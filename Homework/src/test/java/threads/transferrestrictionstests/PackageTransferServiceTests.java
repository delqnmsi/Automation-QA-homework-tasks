package threads.transferrestrictionstests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Test;
import threads.PackageTransferService;
import threads.TransferException;
import threads.models.FileTransferResult;
import threads.models.PackageTransferReport;
import threads.models.SimpleDownloadInfo;
import threads.services.DownloadInfo;
import threads.testutils.FakeTransferServices;
import threads.testutils.TransferTestFileNames;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Epic("Package Transfer Pipeline")
@Feature("Download-to-Upload Orchestration")
public class PackageTransferServiceTests {

    private static final long PACKAGE_ID = 1L;

    private final TransferTestFileNames fileNames = new TransferTestFileNames();

    @Test
    @Story("Happy path")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Every file in the package is downloaded and uploaded successfully, and the report reflects full success.")
    public void reportsFullSuccess_when_allFilesTransferCleanly() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf(fileNames.alphaName(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.betaName(), 2_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.gammaName(), 3_000));
        FakeTransferServices fakes = new FakeTransferServices(infos);

        try (PackageTransferService service = new PackageTransferService(fakes, fakes)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            assertThat(report.isFullSuccess()).isTrue();
            assertThat(report.getSuccessCount()).isEqualTo(3);
            assertThat(report.getFailureCount()).isZero();
            assertThat(report.getFileResults()).allMatch(FileTransferResult::isSuccess);
            assertThat(fakes.getUploadedKeys()).containsExactlyInAnyOrder(fileNames.alphaKey(), fileNames.betaKey(), fileNames.gammaKey());
        }
    }

    @Test
    @Story("Fail-fast validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("A restriction violation (duplicate file name) is rejected before any download or upload is attempted.")
    public void throwsBeforeTransferringAnything_when_combinedRestrictionsAreViolated() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf(fileNames.duplicateName(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.duplicateName(), 2_000));
        FakeTransferServices fakes = new FakeTransferServices(infos);

        try (PackageTransferService service = new PackageTransferService(fakes, fakes)) {
            assertThatExceptionOfType(TransferException.class)
                    .isThrownBy(() -> service.transferPackage(PACKAGE_ID))
                    .withMessageContaining(fileNames.duplicateName());

            assertThat(fakes.getUploadedKeys()).isEmpty();
        }
    }

    @Test
    @Story("Per-file failure isolation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("One file failing to download does not abort the other files in the same package.")
    public void isolatesPerFileFailure_when_oneFileFailsToDownload() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf(fileNames.good1Name(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.brokenName(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.good2Name(), 1_000));
        FakeTransferServices fakes = new FakeTransferServices(infos).failDownload(fileNames.brokenName());

        try (PackageTransferService service = new PackageTransferService(fakes, fakes)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            assertThat(report.getSuccessCount()).isEqualTo(2);
            assertThat(report.getFailureCount()).isEqualTo(1);

            FileTransferResult failure = report.getFailures().get(0);
            assertThat(failure.getFileName()).isEqualTo(fileNames.brokenName());
            assertThat(failure.getErrorMessage()).contains(fileNames.brokenName());

            assertThat(fakes.getUploadedKeys()).containsExactlyInAnyOrder(fileNames.good1Key(), fileNames.good2Key());
        }
    }

    @Test
    @Story("Per-file failure isolation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("One file failing to upload does not abort the other files in the same package.")
    public void isolatesPerFileFailure_when_oneFileFailsToUpload() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf(fileNames.good1Name(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.rejectedName(), 1_000));
        FakeTransferServices fakes = new FakeTransferServices(infos).failUpload(fileNames.rejectedName());

        try (PackageTransferService service = new PackageTransferService(fakes, fakes)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            assertThat(report.getSuccessCount()).isEqualTo(1);
            assertThat(report.getFailureCount()).isEqualTo(1);
            assertThat(report.getFailures().get(0).getFileName()).isEqualTo(fileNames.rejectedName());
            assertThat(fakes.getUploadedKeys()).containsExactly(fileNames.good1Key());
        }
    }

    @Test
    @Story("Reporting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Each file's transfer duration is recorded and reflects at least its known artificial delay.")
    public void recordsPerFileTransferDuration_when_fileTransferHasKnownDelay() {
        List<DownloadInfo> infos = List.of(SimpleDownloadInfo.downloadInfoOf(fileNames.slowName(), 1_000));
        FakeTransferServices fakes = new FakeTransferServices(infos).delayDownload(fileNames.slowName(), 50);

        try (PackageTransferService service = new PackageTransferService(fakes, fakes)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            FileTransferResult result = report.getFileResults().get(0);
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getUploadTime()).isGreaterThanOrEqualTo(Duration.ofMillis(50));
        }
    }

    @Test
    @Story("Parallel execution")
    @Severity(SeverityLevel.NORMAL)
    @Description("Multiple slow file transfers run concurrently, finishing well under the time a sequential run would take.")
    public void transfersFilesConcurrently_when_multipleFilesAreSlow() throws InterruptedException {
        int fileCount = 4;
        long delayMillis = 200;
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf(fileNames.f1Name(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.f2Name(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.f3Name(), 1_000),
                SimpleDownloadInfo.downloadInfoOf(fileNames.f4Name(), 1_000));
        FakeTransferServices fakes = givenFilesWithDownloadDelay(infos, delayMillis);

        ExecutorService executor = Executors.newFixedThreadPool(fileCount);
        try (PackageTransferService service = new PackageTransferService(fakes, fakes, executor)) {
            long start = System.nanoTime();
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);
            long elapsedMillis = (System.nanoTime() - start) / 1_000_000;

            thenTransferSucceeded(report);
            thenElapsedTimeIsBelow(elapsedMillis, fileCount * delayMillis);
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    @Story("Bounded memory under parallelism")
    @Severity(SeverityLevel.BLOCKER)
    @Description("5 x 30MB files (150MB requested) never let combined in-flight bytes exceed the 100MB budget, while still running more than one transfer at a time.")
    public void boundsCombinedInFlightBytesToTheCap_when_manyLargeFilesOverlap() throws InterruptedException {
        int fileSize = 30 * 1024 * 1024;
        int fileCount = 5;
        long delayMillis = 150;

        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf(fileNames.big1Name(), fileSize),
                SimpleDownloadInfo.downloadInfoOf(fileNames.big2Name(), fileSize),
                SimpleDownloadInfo.downloadInfoOf(fileNames.big3Name(), fileSize),
                SimpleDownloadInfo.downloadInfoOf(fileNames.big4Name(), fileSize),
                SimpleDownloadInfo.downloadInfoOf(fileNames.big5Name(), fileSize));
        FakeTransferServices fakes = givenFilesWithDownloadDelay(infos, delayMillis);

        ExecutorService executor = Executors.newFixedThreadPool(fileCount);
        try (PackageTransferService service = new PackageTransferService(fakes, fakes, executor)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            thenTransferSucceeded(report);
            thenAllFilesWereUploaded(fakes, fileCount);
            thenPeakInFlightBytesStayedWithinBudget(fakes);
            thenMoreThanOneFileTransferredAtOnce(fakes, fileSize);
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Step("Configure each file to simulate a {1}ms slow download")
    private FakeTransferServices givenFilesWithDownloadDelay(List<DownloadInfo> infos, long delayMillis) {
        FakeTransferServices fakes = new FakeTransferServices(infos);
        infos.forEach(info -> fakes.delayDownload(info.getOriginalFileName(), delayMillis));
        return fakes;
    }

    @Step("Assert the package report reflects full success")
    private void thenTransferSucceeded(PackageTransferReport report) {
        assertThat(report.isFullSuccess()).isTrue();
    }

    @Step("Assert the whole transfer finished under {1}ms (would be sequential otherwise)")
    private void thenElapsedTimeIsBelow(long elapsedMillis, long sequentialUpperBoundMillis) {
        assertThat(elapsedMillis).isLessThan(sequentialUpperBoundMillis);
    }

    @Step("Assert all {1} files were uploaded")
    private void thenAllFilesWereUploaded(FakeTransferServices fakes, int fileCount) {
        assertThat(fakes.getUploadedKeys()).hasSize(fileCount);
    }

    @Step("Assert peak combined in-flight bytes never exceeded the 100MB memory budget")
    private void thenPeakInFlightBytesStayedWithinBudget(FakeTransferServices fakes) {
        assertThat(fakes.getPeakInFlightBytes())
                .isLessThanOrEqualTo((int) threads.TransferRestrictions.MAX_DOWNLOAD_SIZE_ALLOWED_100MB);
    }

    @Step("Assert more than one file transferred at once within that budget")
    private void thenMoreThanOneFileTransferredAtOnce(FakeTransferServices fakes, int fileSize) {
        assertThat(fakes.getPeakInFlightBytes()).isGreaterThanOrEqualTo(2 * fileSize);
    }
}
