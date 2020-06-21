package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "savings_accounts")
public class SavingsAccount extends Account {
    @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5")
    private BigDecimal interestRate;
    protected int secretKey;
    @Enumerated(value = EnumType.STRING)
    protected Status status;
    @DecimalMax(value = "1000", message = "Minimum balance must be below 1000")
    @DecimalMin(value = "100", message = "Minimum balance must be above 100")
    private BigDecimal minimumBalance;

    public SavingsAccount() {
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
    }

    public SavingsAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance,
                          @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5") BigDecimal interestRate,
                          int secretKey, @DecimalMax(value = "1000") @DecimalMin(value = "100") BigDecimal minimumBalance) {
        super(primaryOwner, secondaryOwner, balance);
        setInterestRate(interestRate);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
        setMinimumBalance(minimumBalance);
    }

    public SavingsAccount(AccountHolder primaryOwner, Money balance,
                          @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5") BigDecimal interestRate,
                          int secretKey, @DecimalMax(value = "1000") @DecimalMin(value = "100") BigDecimal minimumBalance) {
        super(primaryOwner, balance);
        setInterestRate(interestRate);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
        setMinimumBalance(minimumBalance);
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate == null ? new BigDecimal("0.0025") : interestRate;
    }

    public int getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(int secretKey) {
        this.secretKey = secretKey;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance == null ? new BigDecimal("1000") : minimumBalance;
    }
}
