package de.ai.tilgungsplan;

import java.util.Scanner;
import java.util.function.Predicate;

class OptionsValidator {

    private static Predicate<String> isNotNull = value -> value != null;
    private static Predicate<String> isBigDecimal = OptionsValidator::isBigDecimal;
    private static Predicate<String> isInteger = OptionsValidator::isInteger;
    private static Predicate<String> isNotNullAndBigNumber = isNotNull.and(isBigDecimal);
    private static Predicate<String> isNotNullAndInteger = isNotNull.and(isInteger);

    private OptionsValidator() {
    }

    static boolean isBigNumberValueValid(String value) {
        return isNotNullAndBigNumber.test(value);
    }

    static boolean isIntegerValueValid(String value) {
        return isNotNullAndInteger.test(value);
    }

    private static boolean isBigDecimal(String value) {
        Scanner scanner = new Scanner(value);
        boolean isBigDecimal = scanner.hasNextBigDecimal();
        scanner.close();
        return isBigDecimal;
    }

    private static boolean isInteger(String value) {
        Scanner scanner = new Scanner(value);
        boolean isInteger = scanner.hasNextInt();
        scanner.close();
        return isInteger;
    }

}

