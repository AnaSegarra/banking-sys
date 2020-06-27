package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@DynamicUpdate
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    private final BigDecimal minimumBalance = new BigDecimal("250");
    @NotNull(message = "Secret key required")
    private int secretKey;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDateTime lastFeeApplied;

    private static final Logger LOGGER = LogManager.getLogger(CheckingAccount.class);

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
        if(months > 0){
            LOGGER.info("Apply " + months + " time(s) the monthly maintenance fee to checking account " + this.getId());
            LOGGER.info("Previous balance: " + balance);
            for(int i = 0; i < months; i++){
                balance.decreaseAmount(monthlyMaintenanceFee);
            }
            LOGGER.info("Balance after deducting the fee: " + balance);
            lastFeeApplied = lastFeeApplied.plusMonths(months);
        }
    }

    @Override
    public String toString() {
        return "CheckingAccount {" +
                "id=" + id +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
