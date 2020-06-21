package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "savings_accounts")
public class SavingsAccount extends Account {
    private BigDecimal interestRate;
    protected int secretKey;
    @Enumerated(value = EnumType.STRING)
    protected Status status;

    public SavingsAccount(BigDecimal interestRate, int secretKey) {
        this.interestRate = interestRate;
        this.secretKey = secretKey;
    }

    public SavingsAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal interestRate, int secretKey) {
        super(primaryOwner, secondaryOwner, balance);
        this.interestRate = interestRate;
        this.secretKey = secretKey;
    }

    public SavingsAccount(AccountHolder primaryOwner, Money balancey, BigDecimal interestRate, int secretKey) {
        super(primaryOwner, balancey);
        this.interestRate = interestRate;
        this.secretKey = secretKey;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
