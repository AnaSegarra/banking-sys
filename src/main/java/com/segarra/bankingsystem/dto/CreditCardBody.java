package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.utils.Money;

import javax.validation.Valid;
import java.math.BigDecimal;

public class CreditCardBody {
    private int primaryOwnerId;
    private int secondaryOwnerId;
    private Money balance;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;

    public CreditCardBody() {
    }

    public CreditCardBody(int primaryOwnerId, int secondaryOwnerId, Money balance,
                          BigDecimal creditLimit, BigDecimal interestRate) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCardBody(int primaryOwnerId, Money balance, BigDecimal creditLimit,
                          BigDecimal interestRate) {
        this.primaryOwnerId = primaryOwnerId;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
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
