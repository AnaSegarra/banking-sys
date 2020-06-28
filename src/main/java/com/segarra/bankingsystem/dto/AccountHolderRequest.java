package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.models.Address;

import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AccountHolderRequest {
    @NotNull(message = "A username is required")
    private String username;
    @NotNull(message = "A password is required")
    private String password;
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Birthday is required")
    private LocalDate birthday;
    @Valid
    @NotNull(message = "A primary address is required")
    private Address primaryAddress;
    @Valid
    private Address mailingAddress;

    public AccountHolderRequest() {
    }

    public AccountHolderRequest(String name, String username, LocalDate birthday, Address primaryAddress, Address mailingAddress) {
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

    @Override
    public String toString() {
        return "AccountHolderRequest{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", primaryAddress=" + primaryAddress +
                ", mailingAddress=" + mailingAddress +
                '}';
    }
}
