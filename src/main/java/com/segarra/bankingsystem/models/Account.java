package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "primary_owner")
    protected AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner")
    protected AccountHolder secondaryOwner;

    @Embedded
    protected Money balance;

    protected BigDecimal penaltyFee;
    protected int secretKey;
    @Enumerated(value = EnumType.STRING)
    protected Status status;

    public Account() {
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   Money balance, BigDecimal penaltyFee, int secretKey) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
    }

    public Account(AccountHolder primaryOwner, Money balance, BigDecimal penaltyFee, int secretKey, Status status) {
        this.primaryOwner = primaryOwner;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.secretKey = secretKey;
        this.status = status;
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

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
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
}
