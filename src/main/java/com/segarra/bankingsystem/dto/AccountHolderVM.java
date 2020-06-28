package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.models.Address;

import java.time.LocalDate;

public class AccountHolderVM {
    private int id;
    private String name;
    private LocalDate birthday;
    private Address primaryAddress;
    private Address mailingAddress;

    public AccountHolderVM() {
    }

    public AccountHolderVM(int id, String name, LocalDate birthday, Address primaryAddress, Address mailingAddress) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
