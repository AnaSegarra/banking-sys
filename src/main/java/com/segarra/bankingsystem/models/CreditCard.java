package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Money;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends Account {
    @DecimalMin(value = "100", message = "Credit limit must be above 100")
    @DecimalMax(value = "100000", message = "Credit limit must be below 100000")
    private BigDecimal creditLimit;
    @DecimalMin(value = "0.1", message = "Interest rate must be above 0.1")
    @DecimalMax(value = "0.2", message = "Interest rate must be below 0.2")
    private BigDecimal interestRate;
    private LocalDateTime lastInterestApplied;

    public CreditCard() {
//        setCreditLimit(creditLimit);
//        setInterestRate(interestRate);
    }

    public CreditCard(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal creditLimit,
                      BigDecimal interestRate) {
        super(primaryOwner, secondaryOwner, balance);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        this.lastInterestApplied = LocalDateTime.now();
    }

    public CreditCard(AccountHolder primaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryOwner, balance);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        this.lastInterestApplied = LocalDateTime.now();
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit == null ? new BigDecimal("100") : creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate == null ? new BigDecimal("0.2") : interestRate;
    }

    public LocalDateTime getLastInterestApplied() {
        return lastInterestApplied;
    }

    public void setLastInterestApplied(LocalDateTime lastInterestApplied) {
        this.lastInterestApplied = lastInterestApplied;
    }

    public void applyMonthlyInterest(){
        int months = Period.between(LocalDate.from(lastInterestApplied), LocalDate.now()).getMonths();
        System.out.println("Los meses que han pasado " + months);
        if(months > 0){
            for(int i = 0; i < months; i++){
                BigDecimal addValue = balance.getAmount()
                        .multiply(interestRate.divide(new BigDecimal("12"),2, RoundingMode.HALF_EVEN));
                balance.increaseAmount(addValue);
            }
            lastInterestApplied = lastInterestApplied.plusMonths(months);
        }
    }
}
