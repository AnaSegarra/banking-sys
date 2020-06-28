package com.segarra.bankingsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ClientUser extends User{
    @OneToOne(mappedBy = "clientUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private AccountHolder accountHolder;

    public ClientUser() {
    }

    public ClientUser(String username, String password) {
        super(username, password);
    }

    public ClientUser(String username, String password, AccountHolder accountHolder) {
        super(username, password);
        this.accountHolder = accountHolder;
    }

    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(AccountHolder accountHolder) {
        this.accountHolder = accountHolder;
    }
}
