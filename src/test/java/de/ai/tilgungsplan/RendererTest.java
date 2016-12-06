package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RendererTest {

    @Mock
    PrintStream output;

    @Mock
    Calculator calculator;

    String renderedTable;

    @Before
    public void setUp() throws Exception {

        MonthlyRedemption monthlyRedemptionStart = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2016, 12, 31))
                .withPayableInterest(BigDecimal.valueOf(123))
                .withPayableDebt(BigDecimal.valueOf(234))
                .withRemainingDebt(BigDecimal.valueOf(123456))
                .withRate(BigDecimal.valueOf(345.33))
                .build();

        MonthlyRedemption monthlyRedemption1 = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2016, 12, 31))
                .withPayableInterest(BigDecimal.valueOf(123))
                .withPayableDebt(BigDecimal.valueOf(234))
                .withRemainingDebt(BigDecimal.valueOf(123456))
                .withRate(BigDecimal.valueOf(345.33))
                .build();

        MonthlyRedemption monthlyRedemption2 = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2017, 1, 31))
                .withPayableInterest(BigDecimal.valueOf(123))
                .withPayableDebt(BigDecimal.valueOf(234))
                .withRemainingDebt(BigDecimal.valueOf(123456))
                .withRate(BigDecimal.valueOf(345.33))
                .build();

        MonthlyRedemption monthlyRedemptionSummary = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2016, 12, 31))
                .withPayableInterest(BigDecimal.valueOf(123))
                .withPayableDebt(BigDecimal.valueOf(234))
                .withRemainingDebt(BigDecimal.valueOf(123456))
                .withRate(BigDecimal.valueOf(345.33))
                .build();

        when(calculator.calculatePlan()).thenReturn(Arrays.asList(monthlyRedemption1, monthlyRedemption2));
        when(calculator.getSummaryAtStartOfFixedInterestDuration()).thenReturn(monthlyRedemptionStart);
        when(calculator.getSummaryAtTheEndOfFixedInterestDuration(anyList())).thenReturn(monthlyRedemptionSummary);
        Renderer renderer = new Renderer(calculator);
        renderedTable = renderer.renderTable();

    }

    @Test
    public void printerShouldRenderTableHeader() throws Exception {
        assertTrue(renderedTable.contains("Tilgung(+)"));
    }

    @Test
    public void printerShouldRenderTableBody() throws Exception {
        assertTrue(renderedTable.contains("345,33 €"));
    }
}