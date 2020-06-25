package com.segarra.bankingsystem.models;

import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User{
    private String name;

    public Admin() {
    }

    public Admin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
