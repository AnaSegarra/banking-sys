package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    private final BigDecimal minimumBalance = new BigDecimal("250");
    private int secretKey;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDateTime lastFeeApplied;

    public CheckingAccount() {
    }

    public CheckingAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                           Money balance, int secretKey) {
        super(primaryOwner, secondaryOwner, balance);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
        this.lastFeeApplied = LocalDateTime.now();
    }

    public CheckingAccount(AccountHolder primaryOwner, Money balance, int secretKey) {
        super(primaryOwner, balance);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
        this.lastFeeApplied = LocalDateTime.now();

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

    public LocalDateTime getLastFeeApplied() {
        return lastFeeApplied;
    }

    public void setLastFeeApplied(LocalDateTime lastFeeApplied) {
        this.lastFeeApplied = lastFeeApplied;
    }

    public void applyMonthlyMaintenanceFee(){
        int months = Period.between(LocalDate.from(lastFeeApplied), LocalDate.now()).getMonths();
        System.out.println("Los meses que han pasado " + months);
        if(months > 0){
            for(int i = 0; i < months; i++){
                System.out.println("el balance antes de aplicar el fee " + balance);
                balance.decreaseAmount(monthlyMaintenanceFee);
                System.out.println("el balance después de aplicar el fee " + balance);
            }
            lastFeeApplied = lastFeeApplied.plusMonths(months);
        }
    }
}
