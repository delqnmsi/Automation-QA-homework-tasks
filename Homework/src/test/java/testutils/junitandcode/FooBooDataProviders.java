package testutils.junitandcode;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class FooBooDataProviders {
    static Stream<Arguments> validTestData() {
        return Stream.of(
                Arguments.of(
                        "0",
                        ""
                ),
                Arguments.of(
                        "0,0,0",
                        ""
                ),
                Arguments.of(
                        "3",
                        "foo"
                ),
                Arguments.of(
                        "5",
                        "bar"
                ),
                Arguments.of(
                        "15",
                        "foobar"
                ),
                Arguments.of(
                        "1,2,4,7",
                        "1,2,4,7"
                ),
                Arguments.of(
                        "3,6,9",
                        "foo,foo,foo"
                ),
                Arguments.of("1 ,2 ,   3",
                        "1,2,foo"

                ),
                Arguments.of(
                        "1,2,3,4,5,6,45",
                        "1,2,foo,4,bar,foo,foobar"
                ),
                Arguments.of(
                        "-1,-2,-3,-4,-5,-6,-45",
                        ""
                ),
                Arguments.of("1,2,3,1,2,3",
                        "1,2,foo,1-copy,2-copy,foo-copy"
                ),
                Arguments.of("0,1,2,3,0,1,2,3",
                        "1,2,foo,1-copy,2-copy,foo-copy"
                ),
                Arguments.of("1,2,3,1,2,3,1,2,3,1,2,3",
                        "1,2,foo,1-copy,2-copy,foo-copy,1-copy,2-copy,foo-copy,1-copy,2-copy,foo-copy"
                ),

                Arguments.of("1,1,3,3,5,5,45,45",
                        "1,1-copy,foo,foo-copy,bar,bar-copy,foobar,foobar-copy"
                ),
                Arguments.of(String.valueOf((long) Integer.MAX_VALUE),
                        String.valueOf((long) Integer.MAX_VALUE)
                ),
                Arguments.of(String.valueOf((long) Integer.MIN_VALUE),
                        ""
                )

        );
    }

    static Stream<Arguments> invalidTestData() {
        return Stream.of(
                Arguments.of(null,
                        "The input can not be null or blank"
                ),
                Arguments.of("",
                        "The input can not be null or blank"
                ),
                Arguments.of("        ",
                        "The input can not be null or blank"
                ),
                Arguments.of("1,2,Ivan",
                        "Invalid Int value: Ivan"
                ),
                Arguments.of("1,2,§",
                        "Invalid Int value: §"
                ),
                Arguments.of("1,,2,3",
                        "Invalid Int value: "

                ),
                Arguments.of("1,2.5,3",
                        "Invalid Int value: 2.5"

                ),
                Arguments.of(String.valueOf((long) Integer.MAX_VALUE + 1),
                        "Invalid Int value: " + ((long) Integer.MAX_VALUE + 1)
                ),
                Arguments.of(String.valueOf((long) Integer.MIN_VALUE - 1),
                        "Invalid Int value: " + String.valueOf((long) Integer.MIN_VALUE - 1)
                )
        );
    }
}
