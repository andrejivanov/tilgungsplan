package de.ai.tilgungsplan;

import de.ai.tilgungsplan.model.MonthlyRedemption;
import de.ai.tilgungsplan.model.Options;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

class Calculator {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_DOWN;
    private Options options;
    private BigDecimal rate;
    private LocalDate applicationDate;

    Calculator(Options options, LocalDate applicationDate) {
        this.options = options;
        this.rate = calculateRate(options);
        this.applicationDate = applicationDate;
    }

    List<MonthlyRedemption> calculatePlan() {
        int numberOfPaydays = calculateNumberOfPaydays();
        LocalDate currentPayday = calculateFirstPayday();
        BigDecimal currentDept = getCreditValue();
        List<MonthlyRedemption> plan = new ArrayList<>(numberOfPaydays+1);
        for (int i = numberOfPaydays; i > 0; i--) {
            MonthlyRedemption monthlyRedemption = calculateMonthlyRedemption(currentPayday, currentDept);
            plan.add(monthlyRedemption);
            currentPayday = calculateNextPayday(currentPayday);
            currentDept = monthlyRedemption.getRemainingDept();
        }
        return plan;
    }

    MonthlyRedemption calculateMonthlyRedemption(final LocalDate payday,
                                                 final BigDecimal remainingDebtFromMonthBefore) {
        BigDecimal payableInterest = calculatePayableInterest(remainingDebtFromMonthBefore);
        BigDecimal payableDebt = calculatePayableDebt(payableInterest);
        BigDecimal remainingDebt = calculateRemainingDebt(remainingDebtFromMonthBefore, payableDebt);

        return MonthlyRedemption.newBuilder()
                .atPayday(payday)
                .withPayableInterest(payableInterest)
                .withPayableDebt(payableDebt)
                .withRemainingDebt(remainingDebt)
                .withRate(this.rate)
                .build();
    }

    private BigDecimal calculatePayableInterest(final BigDecimal remainingDeptFromMonthBefore) {
        return options.getBorrowingRate()
                .multiply(remainingDeptFromMonthBefore)
                .divide(BigDecimal.valueOf(12),ROUNDING_MODE)
                .setScale(2, ROUNDING_MODE);
    }

    private BigDecimal calculatePayableDebt(BigDecimal payableInterest) {
        return this.rate.subtract(payableInterest);
    }

    private BigDecimal calculateRemainingDebt(BigDecimal remainingDebtFromMonthBefore, BigDecimal payableDebt) {
        return remainingDebtFromMonthBefore.subtract(payableDebt);
    }

    LocalDate calculateFirstPayday() {
        return applicationDate.plusMonths(1).with(lastDayOfMonth());
    }

    LocalDate calculateNextPayday(LocalDate previousPayday) {
        return previousPayday.plusMonths(1).with(lastDayOfMonth());
    }

    MonthlyRedemption getSummaryAtStartOfFixedInterestDuration() {
        BigDecimal creditValueInEuro = options.getCreditValueInEuro();
        return MonthlyRedemption.newBuilder()
                .atPayday(applicationDate.with(lastDayOfMonth()))
                .withRemainingDebt(creditValueInEuro)
                .withPayableInterest(BigDecimal.valueOf(0.00).setScale(2,ROUNDING_MODE))
                .withPayableDebt(creditValueInEuro.negate())
                .withRate(creditValueInEuro.negate())
                .build();
    }

    MonthlyRedemption getSummaryAtTheEndOfFixedInterestDuration(List<MonthlyRedemption> plan) {
        BigDecimal remainingDebt = plan.get(plan.size()-1).getRemainingDept();

        BigDecimal sumOfPayedInterest = calculateSumOfPayedInterest(plan);
        BigDecimal sumOfPayedDebt = calculateSumOfPayedDebt(plan);
        BigDecimal sumOfPayedRate = sumOfPayedInterest.add(sumOfPayedDebt);
        return MonthlyRedemption.newBuilder()
                .atPayday(applicationDate)
                .withRemainingDebt(remainingDebt)
                .withPayableInterest(sumOfPayedInterest)
                .withPayableDebt(sumOfPayedDebt)
                .withRate(sumOfPayedRate)
                .build();
    }

    private BigDecimal calculateSumOfPayedDebt(List<MonthlyRedemption> plan) {
        return plan.stream()
                .map(MonthlyRedemption::getPayableDebt)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateSumOfPayedInterest(List<MonthlyRedemption> plan) {
        return plan.stream()
                .map(MonthlyRedemption::getPayableInterest)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    int calculateNumberOfPaydays() {
        return options.getYearsOfFixedInterestRate() * MONTHS_IN_YEAR.intValue();
    }

    private BigDecimal calculateRate(Options options) {
        return options.getCreditValueInEuro()
                .multiply(options.getBorrowingRate().add(options.getFirstRedemptionRate()))
                .divide(MONTHS_IN_YEAR, ROUNDING_MODE)
                .setScale(2, ROUNDING_MODE);
    }

    BigDecimal getCreditValue() {
        return options.getCreditValueInEuro();
    }

}
