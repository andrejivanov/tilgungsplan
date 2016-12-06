package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.function.Predicate;

class OptionsReader {

    private BufferedReader input;
    private PrintStream printStream;

    OptionsReader(BufferedReader input, PrintStream printStream) {
        this.input = input;
        this.printStream = printStream;
    }

    public Options readOptions() {
        String creditValueInEuroInput =
                readBigNumberValue("Höhe des Darlehensbetrages in Euro, zum Beispiel 100000: ",
                        OptionsValidator::isBigNumberValueValid);

        String borrowingPercentInput =
                readBigNumberValue("Sollzins eintragen (in Prozent), zum Beispiel 2,12: ",
                        OptionsValidator::isBigNumberValueValid);

        String firstRedemptionRateInput =
                readBigNumberValue("Anfängliche Tilgung eintragen (in Prozent), zum Beispiel 2: ",
                        OptionsValidator::isBigNumberValueValid);

        String yearsOfFixedInterestRateInput =
                readBigNumberValue("Zinsbinungdauer eintragen (in Jahren), zum Beispiel 10: ",
                        OptionsValidator::isBigNumberValueValid);

        BigDecimal creditValueInEuro = convertToBigDecimal(creditValueInEuroInput);

        String borrowingRateConvertedDecimalPoint = convertFromGermanDecimalMark(borrowingPercentInput);
        BigDecimal borrowingRate =
                convertPercentToRate(convertToBigDecimal(borrowingRateConvertedDecimalPoint));

        String firstRedemptionRateConvertedDecimalPoint = convertFromGermanDecimalMark(firstRedemptionRateInput);
        BigDecimal firstRedemptionRate =
                convertPercentToRate(convertToBigDecimal(firstRedemptionRateConvertedDecimalPoint));


        int yearsOfFixedInterestRate = Integer.parseInt(yearsOfFixedInterestRateInput);

        return Options.newBuilder()
                .withCreditValueInEuro(creditValueInEuro)
                .withBorrowingRate(borrowingRate)
                .withFirstRedemptionRate(firstRedemptionRate)
                .withYearsOfFixedInterestRate(yearsOfFixedInterestRate)
                .build();
    }

    private String readBigNumberValue(String suggestionMessage, Predicate<String> validateFunction) {
        while (true) {
            try {
                printStream.println(suggestionMessage);
                String bigNumberValue = input.readLine();
                boolean isCurrentValueValid = validateFunction.test(bigNumberValue);
                if (!isCurrentValueValid) {
                    printStream.println("Ihre Eingabe '" + bigNumberValue + "' ist ungültig. " +
                            "Bitte, noch mal eintragen.");
                } else {
                    return bigNumberValue;
                }
            } catch (IOException e) {
                printStream.println(e.getMessage());
            }
        }
    }

    private static BigDecimal convertPercentToRate(BigDecimal inPercent) {
        return inPercent.setScale(4, BigDecimal.ROUND_HALF_EVEN)
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_EVEN);
    }

    private static String convertFromGermanDecimalMark(String input){
        return input.replaceAll(",",".");
    }

    private static BigDecimal convertToBigDecimal(String value){
        return BigDecimal.valueOf(Double.parseDouble(value));
    }

}

