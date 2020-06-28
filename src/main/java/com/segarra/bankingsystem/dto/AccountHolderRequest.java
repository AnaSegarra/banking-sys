package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.models.Address;

import java.time.LocalDate;

public class AccountHolderRequest {
    private int id;
    private String name;
    private String username;
    private String password;
    private LocalDate birthday;
    private Address primaryAddress;
    private Address mailingAddress;

    public AccountHolderRequest() {
    }

    public AccountHolderRequest(int id, String name, String username, LocalDate birthday, Address primaryAddress, Address mailingAddress) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.birthday = birthday;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
