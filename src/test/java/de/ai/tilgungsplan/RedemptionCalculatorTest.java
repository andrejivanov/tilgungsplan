package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;
import de.ai.tilgungsplan.model.RedemptionOptions;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class RedemptionCalculatorTest {

    private RedemptionCalculator redemptionCalculator;
    private RedemptionOptions redemptionOptions;

    @Before
    public void setUp() throws Exception {
        redemptionOptions = RedemptionOptions.newBuilder()
                .withCreditValueInEuro(roundedBigDecimal(100000.00))
                .withBorrowingRate(new BigDecimal(0.0212))
                .withFirstRedemptionRate(new BigDecimal(0.02))
                .withYearsOfFixedInterestRate(10)
                .build();
        redemptionCalculator = new RedemptionCalculator(redemptionOptions, LocalDate.of(2016, 12, 4));

    }

    @Test
    public void calculatorShouldGetRightFirstPayday() throws Exception {
        LocalDate firstPayday = redemptionCalculator.calculateFirstPayday();
        assertEquals(LocalDate.of(2017, 1, 31), firstPayday);
    }

    @Test
    public void calculatorShouldGetRightDebtAfterFirstPayday() throws Exception {
        BigDecimal debtAfterFirstPayday = redemptionCalculator.calculateRemainingDeptAfterFirstPay();
        assertEquals(roundedBigDecimal(99833.34), debtAfterFirstPayday);
    }

    @Test
    public void calculatorShouldGetRightNextPayday() throws Exception {
        LocalDate previousPayday = LocalDate.of(2017, 1, 31);
        LocalDate nextPayday = redemptionCalculator.calculateNextPayday(previousPayday);
        assertEquals(LocalDate.of(2017, 2, 28), nextPayday);
    }

    @Test
    public void calculatorShouldGetRightNumberOfPaydays() throws Exception {
        int numberOfPaydays = redemptionCalculator.calculateNumberOfPaydays();
        assertEquals(120, numberOfPaydays);
    }

    @Test
    public void calculatorShouldGetRightCreditValue() throws Exception {
        BigDecimal creditValue = redemptionCalculator.getCreditValue();
        assertEquals(roundedBigDecimal(100000.00), creditValue);
    }

    @Test
    public void calculatorShouldComputeCorrectRate() throws Exception {
        BigDecimal rate = redemptionCalculator.calculateRate(redemptionOptions);
        assertEquals(roundedBigDecimal(343.33), rate);
    }

    @Test
    public void calculatorShouldComputeCorrectNextPayableInterest() throws Exception {
        BigDecimal previousDept = roundedBigDecimal(99833.34);
        BigDecimal nextDept = redemptionCalculator.calculatePayableInterest(previousDept);
        BigDecimal expectedNextDept = roundedBigDecimal(176.37);
        assertEquals(expectedNextDept, nextDept);
    }

    @Test
    public void calculatorShouldComputeCorrectMonthlyRedemption() throws Exception {
        BigDecimal previousDebt = roundedBigDecimal(99833.34);
        BigDecimal currentDebt = roundedBigDecimal(99666.38);
        MonthlyRedemption monthlyRedemption = redemptionCalculator
                .calculateMonthlyRedemption(LocalDate.of(2017, 2, 28), previousDebt, currentDebt);

        MonthlyRedemption expectedMonthlyRedemption = MonthlyRedemption.newBuilder()
                .atPayday(LocalDate.of(2017, 2, 28))
                .withRemainingDebt(roundedBigDecimal(99666.38))
                .withPayableInterest(roundedBigDecimal(176.37))
                .withPayableDebt(roundedBigDecimal(166.96))
                .withRate(roundedBigDecimal(343.33))
                .withRemainingDebtAfterPay(roundedBigDecimal(99499.42))
                .build();

        assertEquals(expectedMonthlyRedemption, monthlyRedemption);
    }

    private BigDecimal roundedBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}