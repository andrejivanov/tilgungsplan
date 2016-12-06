package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

class Renderer {

    private Calculator calculator;

    Renderer(Calculator calculator) {
        this.calculator = calculator;
    }

    String renderTable() {
        V2_AsciiTable asciiTable = new V2_AsciiTable();
        asciiTable.addStrongRule();
        asciiTable.addRow(headerCells().toArray());
        asciiTable.addStrongRule();
        asciiTable.addRow(geFirstLineCells().toArray());

        List<MonthlyRedemption> plan = calculator.calculatePlan();
        Consumer<List<String>> addMonthlyRedemptionCellsToTable = cells -> asciiTable.addRow(cells.toArray());
        plan.stream()
                .map(Renderer::renderMonthlyStep)
                .forEach(addMonthlyRedemptionCellsToTable);



        MonthlyRedemption endOfFixedInterestRateDuration = calculator.getSummaryAtTheEndOfFixedInterestDuration(plan);
        asciiTable.addRow(renderSummary(endOfFixedInterestRateDuration).toArray());
        asciiTable.addStrongRule();
        return printAsciiTable(asciiTable);
    }

    private String printAsciiTable(V2_AsciiTable asciiTable) {
        V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
        rend.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
        rend.setWidth(new WidthAbsoluteEven(100));
        RenderedTable render = rend.render(asciiTable);
        return render.toString();
    }

    private List<String> geFirstLineCells() {
        MonthlyRedemption startOfFixedInterestRateDuration = calculator.getSummaryAtStartOfFixedInterestDuration();
        return renderMonthlyStep(startOfFixedInterestRateDuration);
    }


    private static List<String> renderMonthlyStep(MonthlyRedemption monthlyRedemption) {
        String paydayRendered = renderDate(monthlyRedemption.getPayday());
        String remainingDeptRendered = renderEuroCurrency(monthlyRedemption.getRemainingDept().negate());
        String payableInterestRendered = renderEuroCurrency(monthlyRedemption.getPayableInterest());
        String payableDebtRendered = renderEuroCurrency(monthlyRedemption.getPayableDebt());
        String rateRendered = renderEuroCurrency(monthlyRedemption.getRate());
        return Arrays.asList(paydayRendered, remainingDeptRendered, payableInterestRendered, payableDebtRendered,
                rateRendered);
    }

    private static List<String> renderSummary(MonthlyRedemption summaryMontlyRedemption) {
        String endOfFixedRateDurationGerman = "Zinsbindungsende";
        String remainingDeptRendered = renderEuroCurrency(summaryMontlyRedemption.getRemainingDept().negate());
        String payableInterestRendered = renderEuroCurrency(summaryMontlyRedemption.getPayableInterest());
        String payableDebtRendered = renderEuroCurrency(summaryMontlyRedemption.getPayableDebt());
        String rateRendered = renderEuroCurrency(summaryMontlyRedemption.getRate());
        return Arrays.asList(endOfFixedRateDurationGerman, remainingDeptRendered, payableInterestRendered,
                payableDebtRendered, rateRendered);
    }

    private static String renderDate(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd.MM.YYYY").format(date);
    }

    private static String renderEuroCurrency(BigDecimal value) {
        NumberFormat german = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return german.format(value);
    }

    private static List<String> headerCells() {
        return Arrays.asList("Datum", "Restschuld", "Zinsen", "Tilgung(+)/Auszahlung(-)", "Rate");
    }

}
