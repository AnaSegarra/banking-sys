package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.models.Money;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountRequest {
    @Min(value = 1, message = "A primary owner is required to create an account")
    private int primaryOwnerId;
    private int secondaryOwnerId;
    @Valid
    @NotNull(message = "Balance is required")
    private Money balance;
    @Valid
    private String secretKey;
    @NotNull(message = "Account type required: savings, checking or credit-card")
    private String accountType;
    @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5")
    @DecimalMin(value = "0", message = "Interest rate shouldn't be a negative value")
    private BigDecimal savingsInterestRate;
    @DecimalMax(value = "1000", message = "Minimum balance must be below 1000")
    @DecimalMin(value = "100", message = "Minimum balance must be above 100")
    private BigDecimal savingsMinimumBalance;

    @DecimalMin(value = "100", message = "Credit limit must be above 100")
    @DecimalMax(value = "100000", message = "Credit limit must be below 100000")
    private BigDecimal creditCardLimit;
    @DecimalMin(value = "0.1", message = "Interest rate must be above 0.1")
    @DecimalMax(value = "0.2", message = "Interest rate must be below 0.2")
    private BigDecimal cardInterestRate;

    public AccountRequest() {
    }

    public AccountRequest(int primaryOwnerId, int secondaryOwnerId, Money balance, String secretKey,
                          BigDecimal savingsInterestRate, BigDecimal savingsMinimumBalance,
                          BigDecimal creditCardLimit, BigDecimal cardInterestRate, String accountType) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = balance;
        this.secretKey = secretKey;
        this.savingsInterestRate = savingsInterestRate;
        this.savingsMinimumBalance = savingsMinimumBalance;
        this.creditCardLimit = creditCardLimit;
        this.cardInterestRate = cardInterestRate;
        this.accountType = accountType;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public BigDecimal getSavingsInterestRate() {
        return savingsInterestRate;
    }

    public void setSavingsInterestRate(BigDecimal savingsInterestRate) {
        this.savingsInterestRate = savingsInterestRate;
    }

    public BigDecimal getSavingsMinimumBalance() {
        return savingsMinimumBalance;
    }

    public void setSavingsMinimumBalance(BigDecimal savingsMinimumBalance) {
        this.savingsMinimumBalance = savingsMinimumBalance;
    }

    public BigDecimal getCreditCardLimit() {
        return creditCardLimit;
    }

    public void setCreditCardLimit(BigDecimal creditCardLimit) {
        this.creditCardLimit = creditCardLimit;
    }

    public BigDecimal getCardInterestRate() {
        return cardInterestRate;
    }

    public void setCardInterestRate(BigDecimal cardInterestRate) {
        this.cardInterestRate = cardInterestRate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
