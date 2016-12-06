package de.ai.tilgungsplan.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonthlyRedemption {
    private LocalDate payday;
    private BigDecimal remainingDept;
    private BigDecimal payableInterest;
    private BigDecimal payableDept;
    private BigDecimal rate;

    private MonthlyRedemption(Builder builder) {
        this.payday = builder.payday;
        this.remainingDept = builder.remainingDept;
        this.payableInterest = builder.payableInterest;
        this.payableDept = builder.payableDept;
        this.rate = builder.rate;
    }

    public LocalDate getPayday() {
        return payday;
    }

    public BigDecimal getRemainingDept() {
        return remainingDept;
    }

    public BigDecimal getPayableInterest() {
        return payableInterest;
    }

    public BigDecimal getPayableDebt() {
        return payableDept;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

     public static final class Builder {
        private LocalDate payday;
        private BigDecimal remainingDept;
        private BigDecimal payableInterest;
        private BigDecimal payableDept;
        private BigDecimal rate;

        public Builder atPayday(LocalDate payday) {
            this.payday = payday;
            return this;
        }

        public Builder withRemainingDebt(BigDecimal remainingDept) {
            this.remainingDept = remainingDept;
            return this;
        }

        public Builder withPayableInterest(BigDecimal payableInterest) {
            this.payableInterest = payableInterest;
            return this;
        }

        public Builder withPayableDebt(BigDecimal payableDept) {
            this.payableDept = payableDept;
            return this;
        }

        public Builder withRate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public MonthlyRedemption build() {
            return new MonthlyRedemption(this);
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

        MonthlyRedemption that = (MonthlyRedemption) o;

        if (!payday.equals(that.payday)) {
            return false;
        }
        if (!remainingDept.equals(that.remainingDept)) {
            return false;
        }
        if (!payableInterest.equals(that.payableInterest)) {
            return false;
        }
        if (!payableDept.equals(that.payableDept)) {
            return false;
        }
        return rate.equals(that.rate);

    }

    @Override
    public int hashCode() {
        int result = payday.hashCode();
        result = 31 * result + remainingDept.hashCode();
        result = 31 * result + payableInterest.hashCode();
        result = 31 * result + payableDept.hashCode();
        result = 31 * result + rate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MonthlyRedemption{" +
                "payday=" + payday +
                ", remainingDept=" + remainingDept +
                ", payableInterest=" + payableInterest +
                ", payableDept=" + payableDept +
                ", rate=" + rate +
                '}';
    }
}
