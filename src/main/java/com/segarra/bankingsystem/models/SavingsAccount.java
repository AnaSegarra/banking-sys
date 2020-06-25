package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "savings_accounts")
public class SavingsAccount extends Account {
    @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5")
    @DecimalMin(value = "0", message = "Interest rate shouldn't be a negative value")
    @Column(columnDefinition = "DECIMAL(5,4)")
    private BigDecimal interestRate;
    protected int secretKey;
    @Enumerated(value = EnumType.STRING)
    protected Status status;
    @DecimalMax(value = "1000", message = "Minimum balance must be below 1000")
    @DecimalMin(value = "100", message = "Minimum balance must be above 100")
    private BigDecimal minimumBalance;
    private LocalDateTime lastInterestApplied;

    private static final Logger LOGGER = LogManager.getLogger(SavingsAccount.class);

    public SavingsAccount() {
    }

    public SavingsAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance,
                          BigDecimal interestRate, int secretKey, BigDecimal minimumBalance) {
        super(primaryOwner, secondaryOwner, balance);
        this.secretKey = secretKey;
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        this.status = Status.ACTIVE;
        this.lastInterestApplied = LocalDateTime.now();
    }

    public SavingsAccount(AccountHolder primaryOwner, Money balance, BigDecimal interestRate, int secretKey,
                          BigDecimal minimumBalance) {
        super(primaryOwner, balance);
        this.secretKey = secretKey;
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        this.status = Status.ACTIVE;
        this.lastInterestApplied = LocalDateTime.now();
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate == null ? new BigDecimal("0.0025") : interestRate;
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

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance == null ? new BigDecimal("1000") : minimumBalance;
    }

    public LocalDateTime getLastInterestApplied() {
        return lastInterestApplied;
    }

    public void setLastInterestApplied(LocalDateTime lastInterestApplied) {
        this.lastInterestApplied = lastInterestApplied;
    }

    public void applyAnnualInterest(){
        int years = Period.between(LocalDate.from(lastInterestApplied), LocalDate.now()).getYears();
        if(years > 0){
            LOGGER.info("Apply " + years + " time(s) the annual interestRate to savings account " + this.getId());
            for(int i = 0; i < years; i++){
                BigDecimal interest = balance.getAmount().multiply(interestRate);
                LOGGER.info("Balance increased by " + interest);
                balance.increaseAmount(interest);
            }
            lastInterestApplied = lastInterestApplied.plusYears(years);
        }
    }
}
