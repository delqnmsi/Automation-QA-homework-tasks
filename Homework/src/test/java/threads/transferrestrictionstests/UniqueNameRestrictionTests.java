package threads.transferrestrictionstests;

import org.junit.jupiter.api.Test;
import threads.TransferException;
import threads.TransferRestrictions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UniqueNameRestrictionTests {

    @Test
    public void acceptFileNames_when_allNamesAreUnique() {
        List<String> names = List.of("uniqueA.txt", "uniqueB.txt", "uniqueC.txt");
        assertThat(TransferRestrictions.areFileNamesUnique(names)).isTrue();
        assertThatCode(() -> TransferRestrictions.checkFileNamesUnique(names)).doesNotThrowAnyException();
    }

    @Test
    public void throwException_when_fileNamesAreDuplicated() {
        List<String> names = List.of("uniqueA.txt", "duplicateA.txt", "duplicateA.txt");
        assertThat(TransferRestrictions.areFileNamesUnique(names)).isFalse();
        assertThatExceptionOfType(TransferException.class)
                .isThrownBy(() -> TransferRestrictions.checkFileNamesUnique(names))
                .withMessageContaining("duplicateA.txt");
    }
}
