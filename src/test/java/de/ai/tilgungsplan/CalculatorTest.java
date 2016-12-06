package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;
import de.ai.tilgungsplan.model.Options;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class CalculatorTest {

    private Calculator calculator;

    @Before
    public void setUp() throws Exception {
        Options options = Options.newBuilder()
                .withCreditValueInEuro(roundedBigDecimal(100000.00))
                .withBorrowingRate(BigDecimal.valueOf(0.0212))
                .withFirstRedemptionRate(BigDecimal.valueOf(0.02))
                .withYearsOfFixedInterestRate(10)
                .build();
        calculator = new Calculator(options, LocalDate.of(2016, 12, 4));

    }

    @Test
    public void calculatorShouldGetRightFirstPayday() throws Exception {
        LocalDate firstPayday = calculator.calculateFirstPayday();
        assertEquals(LocalDate.of(2017, 1, 31), firstPayday);
    }

    @Test
    public void calculatorShouldGetRightNextPayday() throws Exception {
        LocalDate previousPayday = LocalDate.of(2017, 1, 31);
        LocalDate nextPayday = calculator.calculateNextPayday(previousPayday);
        assertEquals(LocalDate.of(2017, 2, 28), nextPayday);
    }

    @Test
    public void calculatorShouldGetRightNumberOfPaydays() throws Exception {
        int numberOfPaydays = calculator.calculateNumberOfPaydays();
        assertEquals(120, numberOfPaydays);
    }

    @Test
    public void calculatorShouldGetRightCreditValue() throws Exception {
        BigDecimal creditValue = calculator.getCreditValue();
        assertEquals(roundedBigDecimal(100000.00), creditValue);
    }


    @Test
    public void calculatorShouldComputeCorrectMonthlyRedemption() throws Exception {
        BigDecimal previousDebt = roundedBigDecimal(99833.34);
        MonthlyRedemption monthlyRedemption = calculator
                .calculateMonthlyRedemption(LocalDate.of(2017, 2, 28), previousDebt);

        MonthlyRedemption expectedMonthlyRedemption = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2017, 2, 28))
                .withRemainingDebt(roundedBigDecimal(99666.38))
                .withPayableInterest(roundedBigDecimal(176.37))
                .withPayableDebt(roundedBigDecimal(166.96))
                .withRate(roundedBigDecimal(343.33))
                .build();

        assertEquals(expectedMonthlyRedemption, monthlyRedemption);
    }

    @Test
    public void calculatorShouldComputeSummaryAtStartOfFixedInterestDuration() throws Exception {
        MonthlyRedemption expectedMonthlyRedemption = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2016, 12, 31))
                .withRemainingDebt(roundedBigDecimal(100000.00))
                .withPayableInterest(roundedBigDecimal(0.00))
                .withPayableDebt(roundedBigDecimal(-100000.00))
                .withRate(roundedBigDecimal(-100000.00))
                .build();
        MonthlyRedemption monthlyRedemption = calculator.getSummaryAtStartOfFixedInterestDuration();

        assertEquals(expectedMonthlyRedemption, monthlyRedemption);
    }

    @Test
    public void calculatorShouldComputeSummaryAtTheEndOfFixedInterestDuration() throws Exception {
        MonthlyRedemption expectedMonthlyRedemption = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2016, 12, 4))
                .withRemainingDebt(roundedBigDecimal(77744.14))
                .withPayableInterest(roundedBigDecimal(18943.74))
                .withPayableDebt(roundedBigDecimal(22255.86))
                .withRate(roundedBigDecimal(41199.60))
                .build();
        List<MonthlyRedemption> plan = calculator.calculatePlan();
        MonthlyRedemption monthlyRedemption = calculator.getSummaryAtTheEndOfFixedInterestDuration(plan);

        assertEquals(expectedMonthlyRedemption, monthlyRedemption);
    }

    private BigDecimal roundedBigDecimal(double value) {
        return BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}