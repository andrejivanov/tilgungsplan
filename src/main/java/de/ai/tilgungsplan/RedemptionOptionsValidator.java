package de.ai.tilgungsplan;

import java.math.BigDecimal;
import java.util.function.Predicate;

class RedemptionOptionsValidator {

    private static Predicate<String> isNotNull = value -> value != null;
    private static Predicate<String> isBigNumber = RedemptionOptionsValidator::isBigNumber;
    private static Predicate<String> isInteger = RedemptionOptionsValidator::isInteger;
    private static Predicate<String> isNotNullAndBigNumber = isNotNull.and(isBigNumber);
    private static Predicate<String> isNotNullAndInteger = isNotNull.and(isInteger);

    static boolean isBigNumberValueValid(String value) {
        return isNotNullAndBigNumber.test(value);
    }

    static boolean isIntegerValueValid(String value) {
        return isNotNullAndInteger.test(value);
    }

    private static boolean isBigNumber(String value) {
        try {
            String convertedFromGermanNotation = value.replaceAll(",", ".");
            new BigDecimal(convertedFromGermanNotation);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    private static boolean isInteger(String value) {
        try {
            new Integer(value);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

}

