package com.segarra.bankingsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class AccountHolder extends User{
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Birthday is required")
    private LocalDate birthday;
    @Valid
    @NotNull(message = "A primary address is required")
    @Embedded
    private Address primaryAddress;
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "mail_country")),
            @AttributeOverride(name = "city", column = @Column(name = "mail_city")),
            @AttributeOverride(name = "street", column = @Column(name = "mail_street")),
            @AttributeOverride(name = "number", column = @Column(name = "mail_number")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "mail_zip_code"))
    })
    private Address mailingAddress;
    @OneToMany(mappedBy = "primaryOwner")
    @JsonIgnore
    private List<Account> accounts;
    @OneToMany(mappedBy = "secondaryOwner")
    @JsonIgnore
    private List<Account> secondaryAccounts;

    public AccountHolder() {
    }

    public AccountHolder(String name, LocalDate birthday, Address primaryAddress, String password, String username) {
        super(username, password);
        this.name = name;
        this.birthday = birthday;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolder(String name, LocalDate birthday, Address primaryAddress, String password,
                         String username, Address mailingAddress) {
        super(username, password);
        this.name = name;
        this.birthday = birthday;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Account> getSecondaryAccounts() {
        return secondaryAccounts;
    }

    public void setSecondaryAccounts(List<Account> secondaryAccounts) {
        this.secondaryAccounts = secondaryAccounts;
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

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", primaryAddress=" + primaryAddress +
                ", mailingAddress=" + mailingAddress +
                '}';
    }
}

