package de.ai.tilgungsplan.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Options {
    private BigDecimal creditValueInEuro;
    private BigDecimal borrowingRate;
    private BigDecimal firstRedemptionRate;
    private int yearsOfFixedInterestRate;

    private Options(Builder builder) {
        creditValueInEuro = builder.creditValueInEuro;
        borrowingRate = builder.borrowingRate;
        firstRedemptionRate = builder.firstRedemptionRate;
        yearsOfFixedInterestRate = builder.yearsOfFixedInterestRate;
    }

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

        public Options build() {
            return new Options(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Options options = (Options) o;
        return yearsOfFixedInterestRate == options.yearsOfFixedInterestRate &&
                Objects.equals(creditValueInEuro, options.creditValueInEuro) &&
                Objects.equals(borrowingRate, options.borrowingRate) &&
                Objects.equals(firstRedemptionRate, options.firstRedemptionRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditValueInEuro, borrowingRate, firstRedemptionRate, yearsOfFixedInterestRate);
    }

    @Override
    public String toString() {
        return "Options{" +
                "creditValueInEuro=" + creditValueInEuro +
                ", borrowingRate=" + borrowingRate +
                ", firstRedemptionRate=" + firstRedemptionRate +
                ", yearsOfFixedInterestRate=" + yearsOfFixedInterestRate +
                '}';
    }
}

