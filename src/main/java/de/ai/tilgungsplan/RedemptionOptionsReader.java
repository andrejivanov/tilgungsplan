package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.RedemptionOptions;

import java.io.*;
import java.math.BigDecimal;
import java.util.function.Predicate;

public class RedemptionOptionsReader {

    private InputStream inputStream;
    private PrintStream printStream;

    public RedemptionOptionsReader(InputStream inputStream, PrintStream printStream) {
        this.inputStream = inputStream;
        this.printStream = printStream;
    }

    public RedemptionOptions readOptions() {
        String creditValueInEuroInput =
                readBigNumberValue("Höhe des Darlehensbetrages in Euro, zum Beispiel 100000: ",
                        RedemptionOptionsValidator::isBigNumberValueValid);

        String borrowingPercentInput =
                readBigNumberValue("Sollzins eintragen (in Prozent), zum Beispiel 2,12: ",
                        RedemptionOptionsValidator::isBigNumberValueValid);

        String firstRedemptionRateInput =
                readBigNumberValue("Anfängliche Tilgung eintragen (in Prozent), zum Beispiel 2: ",
                        RedemptionOptionsValidator::isBigNumberValueValid);

        String yearsOfFixedInterestRateInput =
                readBigNumberValue("Zinsbinungdauer eintragen (in Jahren), zum Beispiel 10: ",
                        RedemptionOptionsValidator::isBigNumberValueValid);

        BigDecimal creditValueInEuro = new BigDecimal(creditValueInEuroInput);
        BigDecimal borrowingRate = convertPercentToRate(new BigDecimal(borrowingPercentInput));
        BigDecimal firstRedemtionRate = convertPercentToRate(new BigDecimal(firstRedemptionRateInput));
        int yearsOfFixedInterestRate = new Integer(yearsOfFixedInterestRateInput);

        return RedemptionOptions.newBuilder()
                .withCreditValueInEuro(creditValueInEuro)
                .withBorrowingRate(borrowingRate)
                .withFirstRedemptionRate(firstRedemtionRate)
                .withYearsOfFixedInterestRate(yearsOfFixedInterestRate)
                .build();
    }

    private String readBigNumberValue(String suggestionMessage, Predicate<String> validateFunction) {
        while (true) {
            try {
                printStream.println(suggestionMessage);
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String bigNumberValue = bufferedReader.readLine();
                boolean isCurrentValueValid = validateFunction.test(bigNumberValue);
                if (!isCurrentValueValid) {
                    System.out.println("Ihre Eingabe '" + bigNumberValue + "' ist ungültig. " +
                            "Bitte, noch mal eintragen.");
                } else {
                    return bigNumberValue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static BigDecimal convertPercentToRate(BigDecimal inPercent) {
        return inPercent.divide(BigDecimal.valueOf(100), BigDecimal.ROUND_UP);
    }

}

