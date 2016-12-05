package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;
import de.ai.tilgungsplan.model.RedemptionOptions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

class RedemptionCalculator {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
    private RedemptionOptions redemptionOptions;
    private BigDecimal rate;
    private LocalDate applicationDate;

    RedemptionCalculator(RedemptionOptions redemptionOptions, LocalDate applicationDate) {
        this.redemptionOptions = redemptionOptions;
        this.rate = calculateRate(redemptionOptions);
        this.applicationDate = applicationDate;
    }

    LocalDate calculateFirstPayday() {
        return applicationDate.plusMonths(1).with(lastDayOfMonth());
    }

    LocalDate calculateNextPayday(LocalDate previousPayday) {
        return previousPayday.plusMonths(1).with(lastDayOfMonth());
    }

    int calculateNumberOfPaydays() {
        return redemptionOptions.getYearsOfFixedInterestRate() * MONTHS_IN_YEAR.intValue();
    }

    MonthlyRedemption calculateMonthlyRedemption(final LocalDate payday,
                                                 final BigDecimal remainingDebtFromMonthBefore) {
        BigDecimal payableInterest = calculatePayableInterest(remainingDebtFromMonthBefore);
        BigDecimal payableDebt = calculatePayableDept(payableInterest);
        BigDecimal remainingDebt = calculateRemainingDebt(remainingDebtFromMonthBefore, payableDebt);
        BigDecimal remainingDebtAfterPay = calculateRemainingDeptAfterPay(remainingDebt, payableDebt);

        return MonthlyRedemption.newBuilder()
                .atPayday(payday)
                .withPayableInterest(payableInterest)
                .withPayableDebt(payableDebt)
                .withRemainingDebt(remainingDebt)
                .withRate(this.rate)
                .build();
    }

    private BigDecimal calculateRemainingDebt(BigDecimal remainingDebtFromMonthBefore, BigDecimal payableDebt) {
        return remainingDebtFromMonthBefore.subtract(payableDebt);
    }

    BigDecimal calculateRemainingDeptAfterFirstPay() {
        BigDecimal firstInterestPay = calculatePayableInterest(redemptionOptions.getCreditValueInEuro());
        BigDecimal firstDeptPay = this.rate.subtract(firstInterestPay);
        return redemptionOptions.getCreditValueInEuro().subtract(firstDeptPay);
    }

    private BigDecimal calculatePayableDept(BigDecimal payableInterest) {
        return this.rate.subtract(payableInterest);
    }

    BigDecimal calculateRate(RedemptionOptions redemptionOptions) {
        return redemptionOptions.getCreditValueInEuro()
                .multiply(redemptionOptions.getBorrowingRate().add(redemptionOptions.getFirstRedemptionRate()))
                .divide(MONTHS_IN_YEAR, ROUNDING_MODE)
                .setScale(2, ROUNDING_MODE);
    }

    BigDecimal calculatePayableInterest(final BigDecimal remainingDeptFromMonthBefore) {
        return remainingDeptFromMonthBefore
                .multiply(redemptionOptions.getBorrowingRate().divide(MONTHS_IN_YEAR, ROUNDING_MODE))
                .setScale(2, ROUNDING_MODE);
    }

    BigDecimal getCreditValue() {
        return redemptionOptions.getCreditValueInEuro();
    }

    private BigDecimal calculateRemainingDeptAfterPay(BigDecimal remainingDept, BigDecimal payableDept) {
        return remainingDept.subtract(payableDept);
    }
}
