package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {
    private BigDecimal monthlyMaintenanceFee;
    private BigDecimal minimumBalance;

    public CheckingAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                           Money balance, BigDecimal penaltyFee, int secretKey,
                           BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(primaryOwner, secondaryOwner, balance, penaltyFee, secretKey);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(AccountHolder primaryOwner, Money balance, BigDecimal penaltyFee,
                           int secretKey, Status status, BigDecimal monthlyMaintenanceFee,
                           BigDecimal minimumBalance) {
        super(primaryOwner, balance, penaltyFee, secretKey, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }

    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
