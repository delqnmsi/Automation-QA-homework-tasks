package threads.transferrestrictionstests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threads.PackageTransferService;
import threads.models.FileTransferResult;
import threads.models.PackageTransferReport;
import threads.models.SimpleDownloadInfo;
import threads.services.DownloadInfo;
import threads.services.DownloadService;
import threads.services.UploadService;
import threads.testutils.TransferTestFileNames;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageTransferServiceMockitoExampleTest {

    private static final long PACKAGE_ID = 1L;

    private final TransferTestFileNames fileNames = new TransferTestFileNames();

    @Mock
    private DownloadService downloadService;

    @Mock
    private UploadService uploadService;

    @Test
    public void transfersAllFiles_when_downloadAndUploadSucceed() throws Exception {
        DownloadInfo alpha = SimpleDownloadInfo.downloadInfoOf(fileNames.alphaName(), 4);
        DownloadInfo beta = SimpleDownloadInfo.downloadInfoOf(fileNames.betaName(), 8);
        when(downloadService.getDownloadInfos(PACKAGE_ID)).thenReturn(List.of(alpha, beta));
        when(downloadService.download(alpha)).thenReturn(new ByteArrayInputStream(new byte[4]));
        when(downloadService.download(beta)).thenReturn(new ByteArrayInputStream(new byte[8]));

        try (PackageTransferService service = new PackageTransferService(downloadService, uploadService)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            assertThat(report.isFullSuccess()).isTrue();
            assertThat(report.getSuccessCount()).isEqualTo(2);

            verify(downloadService).download(alpha);
            verify(downloadService).download(beta);
            verify(uploadService, times(2)).doUpload(anyString(), any(InputStream.class), anyInt());
        }
    }

    @Test
    public void capturesExactUploadArguments_when_fileTransfers() throws Exception {
        DownloadInfo file = SimpleDownloadInfo.downloadInfoOf(fileNames.reportName(), 10);
        when(downloadService.getDownloadInfos(PACKAGE_ID)).thenReturn(List.of(file));
        when(downloadService.download(file)).thenReturn(new ByteArrayInputStream(new byte[10]));

        try (PackageTransferService service = new PackageTransferService(downloadService, uploadService)) {
            service.transferPackage(PACKAGE_ID);
        }

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(uploadService).doUpload(keyCaptor.capture(), any(InputStream.class), sizeCaptor.capture());

        assertThat(keyCaptor.getValue()).isEqualTo(file.getFileKey());
        assertThat(sizeCaptor.getValue()).isEqualTo(10);
    }

    @Test
    public void neverCallsUploadService_when_downloadThrows() throws Exception {
        DownloadInfo broken = SimpleDownloadInfo.downloadInfoOf(fileNames.brokenName(), 5);
        when(downloadService.getDownloadInfos(PACKAGE_ID)).thenReturn(List.of(broken));
        when(downloadService.download(broken)).thenThrow(new RuntimeException("simulated download failure"));

        try (PackageTransferService service = new PackageTransferService(downloadService, uploadService)) {
            PackageTransferReport report = service.transferPackage(PACKAGE_ID);

            assertThat(report.getFailureCount()).isEqualTo(1);
            FileTransferResult failure = report.getFileResults().get(0);
            assertThat(failure.isSuccess()).isFalse();
            assertThat(failure.getErrorMessage()).contains("simulated download failure");

            verify(uploadService, never()).doUpload(any(), any(), anyInt());
        }
    }
}
