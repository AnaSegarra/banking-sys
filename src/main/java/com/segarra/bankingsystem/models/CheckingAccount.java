package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    private final BigDecimal minimumBalance = new BigDecimal("250");
    protected int secretKey;
    @Enumerated(value = EnumType.STRING)
    protected Status status;

    public CheckingAccount() {
        this.status = Status.ACTIVE;
    }

    public CheckingAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                           Money balance, int secretKey) {
        super(primaryOwner, secondaryOwner, balance);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
    }

    public CheckingAccount(AccountHolder primaryOwner, Money balance, int secretKey) {
        super(primaryOwner, balance);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
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

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "monthlyMaintenanceFee=" + monthlyMaintenanceFee +
                ", minimumBalance=" + minimumBalance +
                ", primaryOwner=" + primaryOwner +
                ", secondaryOwner=" + secondaryOwner +
                ", balance=" + balance +
                ", penaltyFee=" + penaltyFee +
                ", secretKey=" + secretKey +
                ", status=" + status +
                '}';
    }
}
