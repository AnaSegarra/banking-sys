package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends Account{
    private BigDecimal creditLimit;
    private BigDecimal interestRate;

    public CreditCard() {
    }

    public CreditCard(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryOwner, secondaryOwner, balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryOwner, balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
