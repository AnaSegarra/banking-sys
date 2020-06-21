package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Money;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends Account {
    @DecimalMin(value = "100", message = "Credit limit must be above 100")
    @DecimalMax(value = "100000", message = "Credit limit must be below 100000")
    private BigDecimal creditLimit;
    @DecimalMin(value = "0.1", message = "Interest rate must be above 0.1")
    @DecimalMax(value = "0.2", message = "Interest rate must be below 0.2")
    private BigDecimal interestRate;

    public CreditCard() {
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCard(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryOwner, secondaryOwner, balance);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCard(AccountHolder primaryOwner, Money balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryOwner, balance);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
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
}
