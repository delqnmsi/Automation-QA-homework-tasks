package jUnitAndCode;

import junitAndCode.FooBar;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FooBarTests {
    private static final Logger logger = LoggerFactory.getLogger(FooBarTests.class);

    @ParameterizedTest
    @MethodSource("testutils.junitandcode.FooBooDataProviders#validTestData")
    public void fooBooFunctionWorksProperly_when_differentInputProvided(String input, String expectedResult) {
        FooBar processor = new FooBar();

        var actualResult = processor.foobar(input);

        logger.info("Expected Result: '{}'", expectedResult);
        logger.info("Actual Result: '{}'", actualResult);

        assertEquals(expectedResult, actualResult);

    }

    @ParameterizedTest
    @MethodSource("testutils.junitandcode.FooBooDataProviders#invalidTestData")
    public void fooBooFunctionThrowsException_when_nonIntInputProvided(String input, String message) {
        FooBar processor = new FooBar();

        logger.info("Input Value: '{}'", input);
        logger.info("Message: '{}'", message);

        assertThrows(
                IllegalArgumentException.class,
                () -> processor.foobar(input)
        );
    }
}
