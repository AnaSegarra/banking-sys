package com.segarra.bankingsystem.models;

import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User{
    public Admin() {
    }

    public Admin(String username, String password) {
        super(username, password);
    }
}
