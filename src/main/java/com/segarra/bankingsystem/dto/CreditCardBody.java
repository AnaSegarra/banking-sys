package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.utils.Money;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class CreditCardBody {
    private int primaryOwnerId;
    private int secondaryOwnerId;
    private Money balance;
    @DecimalMin(value = "100", message = "Credit limit must be above 100")
    @DecimalMax(value = "100000", message = "Credit limit must be below 100000")
    private BigDecimal creditLimit;
    @DecimalMin(value = "0.1", message = "Interest rate must be above 0.1")
    @DecimalMax(value = "0.2", message = "Interest rate must be below 0.2")
    private BigDecimal interestRate;

    public CreditCardBody() {
    }

    public int getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(int primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public int getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(int secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
