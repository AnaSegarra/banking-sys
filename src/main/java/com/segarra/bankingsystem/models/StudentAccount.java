package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "student_accounts")
public class StudentAccount extends Account {
    public StudentAccount() {
    }

    public StudentAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, int secretKey) {
        super(primaryOwner, secondaryOwner, balance, secretKey);
    }

    public StudentAccount(AccountHolder primaryOwner, Money balance, int secretKey) {
        super(primaryOwner, balance, secretKey);
    }

    @Override
    public String toString() {
        return "StudentAccount{" +
                "primaryOwner=" + primaryOwner +
                ", secondaryOwner=" + secondaryOwner +
                ", balance=" + balance +
                ", penaltyFee=" + penaltyFee +
                ", secretKey=" + secretKey +
                ", status=" + status +
                '}';
    }
}
