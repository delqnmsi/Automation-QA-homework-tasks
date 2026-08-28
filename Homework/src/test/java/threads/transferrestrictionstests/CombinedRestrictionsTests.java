package threads.transferrestrictionstests;

import org.junit.jupiter.api.Test;
import threads.TransferException;
import threads.TransferRestrictions;
import threads.models.SimpleDownloadInfo;
import threads.services.DownloadInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CombinedRestrictionsTests {

    @Test
    public void acceptInput_when_inputIsClean() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf("testPDFFile.pdf", 1_000),
                SimpleDownloadInfo.downloadInfoOf("testPhotoFile.png", 2_000_000));
        assertThatCode(() -> TransferRestrictions.checkCombinedRestrictions(infos)).doesNotThrowAnyException();
    }

    @Test
    public void throwExceptions_when_InputIsNullOrEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TransferRestrictions.checkCombinedRestrictions(null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TransferRestrictions.checkCombinedRestrictions(List.of()));
    }

    @Test
    public void throwException_when_inputHasForbiddenExtension() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf("testPDFFile.pdf", 1_000),
                SimpleDownloadInfo.downloadInfoOf("testEXEFile.exe", 10));
        assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> TransferRestrictions.checkCombinedRestrictions(infos))
                .withMessageContaining("testEXEFile.exe");
    }

    @Test
    public void throwException_when_inputHasDuplicateNames() {
        List<DownloadInfo> infos = List.of(
                SimpleDownloadInfo.downloadInfoOf("testTextFile.txt", 1_000),
                SimpleDownloadInfo.downloadInfoOf("testTextFile.txt", 2_000));
        assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> TransferRestrictions.checkCombinedRestrictions(infos))
                .withMessageContaining("testTextFile.txt");
    }
}
