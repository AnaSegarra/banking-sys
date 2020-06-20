package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.utils.Money;

import java.math.BigDecimal;

public class CheckingAccountBody {
    private int primaryOwnerId;
    private int secondaryOwnerId;
    private Money balance;
    private int secretKey;

    public CheckingAccountBody() {
    }

    public CheckingAccountBody(int primaryOwnerId, Money balance, int secretKey) {
        this.primaryOwnerId = primaryOwnerId;
        this.balance = balance;
        this.secretKey = secretKey;
    }

    public CheckingAccountBody(int primaryOwnerId, int secondaryOwnerId, Money balance, int secretKey) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = balance;
        this.secretKey = secretKey;
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

    @Override
    public String toString() {
        return "CheckingAccountBody{" +
                "primaryOwnerId=" + primaryOwnerId +
                ", secondaryOwnerId=" + secondaryOwnerId +
                ", balance=" + balance +
                ", secretKey=" + secretKey +
                '}';
    }
}
