package threads.transferrestrictionstests;

import org.junit.jupiter.api.Test;
import threads.TransferException;
import threads.TransferRestrictions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class FileSizeValidationTests {

    @Test
    public void allowesFileInput_when_fileSizeUpToExactlyTheLimit() {
        int exactly100M = (int) TransferRestrictions.MAX_DOWNLOAD_SIZE_ALLOWED_100MB;
        assertThat(TransferRestrictions.fileFitsMaxDownloadSizeAllowed(exactly100M)).isTrue();
        assertThatCode(() -> TransferRestrictions.checkFileFitsMaxDownloadSizeAllowed("tesBinFile.bin", exactly100M))
                .doesNotThrowAnyException();
    }

    @Test
    public void throwException_when_fileSizeExceedsTheLimit() {
        int tooBig = (int) TransferRestrictions.MAX_DOWNLOAD_SIZE_ALLOWED_100MB + 1;
        assertThat(TransferRestrictions.fileFitsMaxDownloadSizeAllowed(tooBig)).isFalse();
        assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> TransferRestrictions.checkFileFitsMaxDownloadSizeAllowed("testTooBigBinFile.bin", tooBig))
                .withMessageContaining("testTooBigBinFile.bin");
    }
}
