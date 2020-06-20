package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    private final BigDecimal minimumBalance = new BigDecimal("250");

    public CheckingAccount() {
    }

    public CheckingAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                           Money balance, int secretKey) {
        super(primaryOwner, secondaryOwner, balance, secretKey);
    }

    public CheckingAccount(AccountHolder primaryOwner, Money balance, int secretKey) {
        super(primaryOwner, balance, secretKey);
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
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
