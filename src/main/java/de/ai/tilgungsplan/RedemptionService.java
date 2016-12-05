package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.RedemptionOptions;

import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RedemptionService {

    private InputStream inputStream;
    private PrintStream printStream;
    private LocalDate applicationDay;

    public RedemptionService(InputStream inputStream, PrintStream printStream, LocalDate applicationDay){
        this.inputStream = inputStream;
        this.printStream = printStream;
        this.applicationDay = applicationDay;
    }

    public void createAndPrintRedemptionPlan(){

        RedemptionOptionsReader redemptionOptionsReader = new RedemptionOptionsReader(inputStream, printStream);
//        RedemptionOptions redemptionOptions = redemptionOptionsReader.readOptions();
        RedemptionOptions redemptionOptions = RedemptionOptions.newBuilder()
                .withCreditValueInEuro(new BigDecimal(100000.00))
                .withBorrowingRate(new BigDecimal(0.0212))
                .withFirstRedemptionRate(new BigDecimal(0.02))
                .withYearsOfFixedInterestRate(10)
                .build();

        RedemptionCalculator redemptionCalculator = new RedemptionCalculator(redemptionOptions, applicationDay);

        RedemptionPrinter redemptionPrinter = new RedemptionPrinter(redemptionCalculator, printStream);
        redemptionPrinter.printTable();
    }

}
