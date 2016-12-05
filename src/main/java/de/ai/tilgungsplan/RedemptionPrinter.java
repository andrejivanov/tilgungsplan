package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class RedemptionPrinter {

    private RedemptionCalculator redemptionCalculator;
    private PrintStream printStream;

    public RedemptionPrinter(RedemptionCalculator redemptionCalculator, PrintStream printStream) {

        this.redemptionCalculator = redemptionCalculator;
        this.printStream = printStream;
    }

    public void printTable() {
        print(renderHeader());
        printTableBody();
        print(renderEnding());

    }

    private String renderEnding() {
        return "End";
    }

    private void printTableBody() {
        int numberOfPaydays = redemptionCalculator.calculateNumberOfPaydays();
        LocalDate currentPayday = redemptionCalculator.calculateFirstPayday();
        BigDecimal currentDept = redemptionCalculator.getCreditValue();

        for (int i = numberOfPaydays; i > 0; i--) {
            MonthlyRedemption monthlyRedemption = redemptionCalculator
                    .calculateMonthlyRedemption(currentPayday, currentDept);
            String renderedStep = renderMonthlyStep(monthlyRedemption);
            print(renderedStep);
            currentPayday = currentPayday.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            currentDept = monthlyRedemption.getRemainingDept();
        }
    }

    public static String renderMonthlyStep(final MonthlyRedemption monthlyRedemption) {
        StringBuilder redemptionTableLine = new StringBuilder();
        final String paydayRendered = renderDate(monthlyRedemption.getPayday());
        final String remainingDeptRendered = renderEuroCurrency(monthlyRedemption.getRemainingDept());
        final String payableInterestRendered = renderEuroCurrency(monthlyRedemption.getPayableInterest());
        final String payableDebtRendered = renderEuroCurrency(monthlyRedemption.getPayableDebt());
        final String rateRendered = renderEuroCurrency(monthlyRedemption.getRate());
        return redemptionTableLine
                .append(paydayRendered)
                .append(tab())
                .append(remainingDeptRendered)
                .append(tab())
                .append(payableInterestRendered)
                .append(tab())
                .append(payableDebtRendered)
                .append(tab()).append(tab()).append(tab()).append(tab()).append(tab())
                .append(rateRendered)
                .toString();
    }

    private static String renderDate(final LocalDate date) {
        return DateTimeFormatter.ofPattern("dd.MM.YYYY").format(date);
    }

    private static String renderEuroCurrency(final BigDecimal value) {
        NumberFormat german = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return german.format(value);
    }

    private void print(String output) {
        this.printStream.println(output);
    }

    private static String tab() {
        return "\t";
    }

    public static String renderHeader() {
        StringBuilder header = new StringBuilder();

        return header
                .append("Datum")
                .append(tab())
                .append(tab())
                .append("Restschuld")
                .append(tab())
                .append("Zinsen")
                .append(tab()).append(tab())
                .append("Tilgung(+)/Auszahlung(-)")
                .append(tab())
                .append("Rate")
                .toString();
    }

}
