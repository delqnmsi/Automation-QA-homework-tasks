package threads.transferrestrictionstests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threads.PackageTransferService;
import threads.TransferException;
import threads.models.SimpleDownloadInfo;
import threads.services.DownloadInfo;
import threads.services.DownloadService;
import threads.services.UploadService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Demonstrates verifying a fail-fast guarantee with Mockito: when
 * {@code TransferRestrictions.checkCombinedRestrictions} rejects a package,
 * {@link PackageTransferService} must never touch the download or upload service at all -
 * no wasted I/O on a validation failure.
 */
@ExtendWith(MockitoExtension.class)
public class TransferRestrictionsMockitoExampleTest {

    private static final long PACKAGE_ID = 1L;

    @Mock
    private DownloadService downloadService;

    @Mock
    private UploadService uploadService;

    @Test
    public void neverTouchesEitherService_when_combinedRestrictionsAreViolated() throws Exception {
        DownloadInfo duplicate1 = SimpleDownloadInfo.downloadInfoOf("duplicate.txt", 10);
        DownloadInfo duplicate2 = SimpleDownloadInfo.downloadInfoOf("duplicate.txt", 20);
        when(downloadService.getDownloadInfos(PACKAGE_ID)).thenReturn(List.of(duplicate1, duplicate2));

        try (PackageTransferService service = new PackageTransferService(downloadService, uploadService)) {
            assertThatExceptionOfType(TransferException.class)
                    .isThrownBy(() -> service.transferPackage(PACKAGE_ID))
                    .withMessageContaining("duplicate.txt");
        }

        verify(downloadService, never()).download(any());
        verifyNoInteractions(uploadService);
    }

    @Test
    public void neverTouchesEitherService_when_forbiddenExtensionIsPresent() throws Exception {
        DownloadInfo malicious = SimpleDownloadInfo.downloadInfoOf("payload.exe", 10);
        when(downloadService.getDownloadInfos(PACKAGE_ID)).thenReturn(List.of(malicious));

        try (PackageTransferService service = new PackageTransferService(downloadService, uploadService)) {
            assertThatExceptionOfType(TransferException.class)
                    .isThrownBy(() -> service.transferPackage(PACKAGE_ID))
                    .withMessageContaining("payload.exe");
        }

        verifyNoInteractions(uploadService);
        verify(downloadService, never()).download(any());
    }
}
