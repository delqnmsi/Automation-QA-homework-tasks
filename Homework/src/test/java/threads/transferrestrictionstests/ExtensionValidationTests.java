package threads.transferrestrictionstests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import threads.TransferException;
import threads.TransferRestrictions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ExtensionValidationTests {

    @ParameterizedTest
    @CsvSource({
            "invoice.pdf,       pdf",
            "archive.TAR.GZ,    gz",
            "UPPER.EXE,         exe",
            "README,            ''",
            "archive.,          ''",
            ".bashrc,           ''",
            "no.ext.,           ''",
    })
    public void extractLowerCaseExtension_when_fileNameIsProvided(String fileName, String expected) {
        assertThat(TransferRestrictions.extensionOf(fileName)).isEqualTo(expected);
    }

    @Test
    public void throwException_when_fileNameIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TransferRestrictions.extensionOf(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"run.cmd", "old.com", "lib.dll", "disk.dmg", "setup.exe", "ubuntu.iso", "app.jar", "script.js", "SETUP.EXE"})
    public void throwException_when_extensionIsForbidden(String fileName) {
        assertThat(TransferRestrictions.isExtensionAllowed(fileName)).isFalse();
        assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> TransferRestrictions.checkAllowedExtensions(fileName))
                .withMessageContaining(fileName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invoice.pdf", "photo.png", "notes.txt", "data.json", "README", "music.jarx"})
    public void allowExtension_when_extensionIsNotForbidden(String fileName) {
        assertThat(TransferRestrictions.isExtensionAllowed(fileName)).isTrue();
        assertThatCode(() -> TransferRestrictions.checkAllowedExtensions(fileName)).doesNotThrowAnyException();
    }
}
