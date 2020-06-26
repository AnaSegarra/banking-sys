package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.models.Money;

public class AccountVM {
    private int id;
    private Money balance;
    private String accountType;
    private String primaryOwner;
    private String secondaryOwner;
    private String status;

    public AccountVM() {
    }

    public AccountVM(int id, Money balance, String className, String primaryOwner, String secondaryOwner) {
        this.id = id;
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setAccountType(className);
    }

    public AccountVM(int id, Money balance, String accountType, String primaryOwner, String secondaryOwner, String status) {
        this.id = id;
        this.balance = balance;
        this.accountType = accountType;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType.equals("CreditCard") ? "Credit card" : accountType.replace("Account", " account");
    }

    public String getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(String primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    public String getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(String secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
