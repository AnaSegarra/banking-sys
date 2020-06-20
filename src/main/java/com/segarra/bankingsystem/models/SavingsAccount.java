package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "savings_accounts")
public class SavingsAccount extends Account {
    private BigDecimal interestRate;

    public SavingsAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner,
                          Money balance, BigDecimal penaltyFee, int secretKey,
                          BigDecimal interestRate) {
        super(primaryOwner, secondaryOwner, balance, penaltyFee, secretKey);
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryOwner, Money balance, BigDecimal penaltyFee,
                          int secretKey, Status status, BigDecimal interestRate) {
        super(primaryOwner, balance, penaltyFee, secretKey, status);
        this.interestRate = interestRate;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
