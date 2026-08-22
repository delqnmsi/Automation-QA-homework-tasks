package junitAndCode;

import java.util.HashSet;
import java.util.Set;

public class FooBar {

    protected StringBuilder finalResult = new StringBuilder();
    protected Set<Integer> repeatedNumber = new HashSet<>();

    public String foobar(String stringOfNumbers) {

        isTheInputCorrect(stringOfNumbers);
        String[] numbers = stringOfNumbers.split(",");

        for (String number : numbers) {
            int nextValue = Integer.parseInt(number.strip());
            if (nextValue <= 0) {
                continue;
            }
            String print = getPrintValue(nextValue);

            print = checkForDuplicates(nextValue, print);
            addToRepeatNumbers(nextValue);
            appendToResult(print);
        }
        return String.valueOf(finalResult);
    }

    private void isTheInputCorrect(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("The input can not be null or blank");
        }
        String[] values = input.split(",");

        for (String value : values) {
            try {
                Integer.parseInt(value.strip());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid Int value: " + value);

            }
        }
    }

    private String getPrintValue(int output) {
        String printableResult;

        if (output % 3 == 0 && output % 5 == 0) {
            printableResult = "foobar";
        } else if (output % 5 == 0) {
            printableResult = "bar";
        } else if (output % 3 == 0) {
            printableResult = "foo";
        } else {
            printableResult = String.valueOf(output);
        }
        return printableResult;
    }

    private String checkForDuplicates(int value, String print) {
        if (repeatedNumber.contains(value)) {
            return print + "-copy";
        }
        return print;
    }

    private void addToRepeatNumbers(int value) {
        repeatedNumber.add(value);
    }

    private void appendToResult(String print) {
        if (!finalResult.isEmpty()) {
            finalResult.append(",");
        }
        finalResult.append(print);
    }
}
