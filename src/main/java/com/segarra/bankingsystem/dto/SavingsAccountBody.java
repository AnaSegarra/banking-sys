package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.utils.Money;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class SavingsAccountBody {
    private int primaryOwnerId;
    private int secondaryOwnerId;
    private Money balance;
    private int secretKey;
    @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5")
    private BigDecimal interestRate;
    @DecimalMax(value = "1000", message = "Minimum balance must be below 1000")
    @DecimalMin(value = "100", message = "Minimum balance must be above 100")
    private BigDecimal minimumBalance;

    public SavingsAccountBody() {
    }

    public SavingsAccountBody(int primaryOwnerId, int secondaryOwnerId, Money balance, int secretKey,
                              BigDecimal interestRate, BigDecimal minimumBalance) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = balance;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    public SavingsAccountBody(int primaryOwnerId, Money balance, int secretKey, @Valid BigDecimal interestRate,
                              @Valid BigDecimal minimumBalance) {
        this.primaryOwnerId = primaryOwnerId;
        this.balance = balance;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
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

    public int getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(int secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
