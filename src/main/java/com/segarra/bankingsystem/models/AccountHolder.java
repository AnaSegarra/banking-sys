package com.segarra.bankingsystem.models;

import com.segarra.bankingsystem.utils.Address;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "account_holders")
public class AccountHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDate birthday;

    @Embedded
    private Address primaryAddress;

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
    private List<CreditCard> accounts;

    @OneToMany(mappedBy = "secondaryOwner")
    private List<CreditCard> secondaryAccounts;

    public AccountHolder() {
    }

    public AccountHolder(String name, LocalDate birthday, Address primaryAddress) {
        this.name = name;
        this.birthday = birthday;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolder(String name, LocalDate birthday, Address primaryAddress, Address mailingAddress) {
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

    public List<CreditCard> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<CreditCard> accounts) {
        this.accounts = accounts;
    }

    public List<CreditCard> getSecondaryAccounts() {
        return secondaryAccounts;
    }

    public void setSecondaryAccounts(List<CreditCard> secondaryAccounts) {
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
}

