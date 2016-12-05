package de.ai.tilgungsplan.model;

import java.math.BigDecimal;

public class RedemptionOptions {
    private BigDecimal creditValueInEuro;
    private BigDecimal borrowingRate;
    private BigDecimal firstRedemptionRate;
    private int yearsOfFixedInterestRate;

    public BigDecimal getCreditValueInEuro() {
        return creditValueInEuro;
    }

    public BigDecimal getBorrowingRate() {
        return borrowingRate;
    }

    public BigDecimal getFirstRedemptionRate() {
        return firstRedemptionRate;
    }

    public int getYearsOfFixedInterestRate() {
        return yearsOfFixedInterestRate;
    }

    private RedemptionOptions(Builder builder) {
        creditValueInEuro = builder.creditValueInEuro;
        borrowingRate = builder.borrowingRate;
        firstRedemptionRate = builder.firstRedemptionRate;
        yearsOfFixedInterestRate = builder.yearsOfFixedInterestRate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private BigDecimal creditValueInEuro;
        private BigDecimal borrowingRate;
        private BigDecimal firstRedemptionRate;
        private int yearsOfFixedInterestRate;

        private Builder() {
        }

        public Builder withCreditValueInEuro(BigDecimal creditValueInEuro) {
            this.creditValueInEuro = creditValueInEuro;
            return this;
        }

        public Builder withBorrowingRate(BigDecimal borrowingRate) {
            this.borrowingRate = borrowingRate;
            return this;
        }

        public Builder withFirstRedemptionRate(BigDecimal firstRedemptionRate) {
            this.firstRedemptionRate = firstRedemptionRate;
            return this;
        }

        public Builder withYearsOfFixedInterestRate(int yearsOfFixedInterestRate) {
            this.yearsOfFixedInterestRate = yearsOfFixedInterestRate;
            return this;
        }

        public RedemptionOptions build() {
            return new RedemptionOptions(this);
        }


    }
}

