package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.utils.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "student_accounts")
public class StudentCheckingAccount extends Account {

    public StudentCheckingAccount(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance, BigDecimal penaltyFee, int secretKey) {
        super(primaryOwner, secondaryOwner, balance, penaltyFee, secretKey);
    }

    public StudentCheckingAccount(AccountHolder primaryOwner, Money balance, BigDecimal penaltyFee, int secretKey, Status status) {
        super(primaryOwner, balance, penaltyFee, secretKey, status);
    }
}
