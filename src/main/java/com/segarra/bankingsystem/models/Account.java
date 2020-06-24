package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Money;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    @ManyToOne
    @JoinColumn(name = "primary_owner")
    protected AccountHolder primaryOwner;
    @ManyToOne
    @JoinColumn(name = "secondary_owner")
    protected AccountHolder secondaryOwner;
    @Embedded
    protected Money balance;
    protected boolean penaltyFeeApplied;
    protected final BigDecimal penaltyFee = new BigDecimal("40");

    public Account() {
        this.penaltyFeeApplied = false;
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner,  Money balance) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
        this.penaltyFeeApplied = false;
    }

    public Account(AccountHolder primaryOwner, Money balance) {
        this.primaryOwner = primaryOwner;
        this.balance = balance;
        this.penaltyFeeApplied = false;
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

    public boolean isPenaltyFeeApplied() {
        return penaltyFeeApplied;
    }

    public void setPenaltyFeeApplied(boolean penaltyFeeApplied) {
        this.penaltyFeeApplied = penaltyFeeApplied;
    }

    public void applyPenaltyFee(BigDecimal minimumBalance){
        // balance below minimumBalance results in a deduction of the penalty fee
        if(balance.getAmount().compareTo(minimumBalance) < 0 && !penaltyFeeApplied){
            balance.decreaseAmount(penaltyFee);
            setPenaltyFeeApplied(true);
        }
    };
}
