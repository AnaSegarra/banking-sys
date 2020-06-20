package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "primary_owner")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner")
    private AccountHolder secondaryOwner;

    @Embedded
    private Money balance;

    private BigDecimal creditLimit;
    private BigDecimal interestRate;
    private BigDecimal penaltyFee;

    public CreditCard() {
    }

    public CreditCard(AccountHolder primaryOwner, Money balance,
                      BigDecimal creditLimit, BigDecimal interestRate, BigDecimal penaltyFee) {
        this.primaryOwner = primaryOwner;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.penaltyFee = penaltyFee;
    }

    public CreditCard(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                      Money balance, BigDecimal creditLimit,
                      BigDecimal interestRate, BigDecimal penaltyFee) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.penaltyFee = penaltyFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }
}
