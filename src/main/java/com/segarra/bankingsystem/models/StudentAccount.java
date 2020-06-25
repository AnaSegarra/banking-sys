package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "student_accounts")
public class StudentAccount extends Account {
    protected int secretKey;
    @Enumerated(value = EnumType.STRING)
    protected Status status;

    public StudentAccount() {
    }

    public StudentAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, int secretKey) {
        super(primaryOwner, secondaryOwner, balance);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
    }

    public StudentAccount(AccountHolder primaryOwner, Money balance, int secretKey) {
        super(primaryOwner, balance);
        this.secretKey = secretKey;
        this.status = Status.ACTIVE;
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
