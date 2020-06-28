package com.segarra.bankingsystem.models;

import javax.persistence.*;

@Entity
public class AdminUser extends User{
    public AdminUser() {
    }

    public AdminUser(String username, String password) {
        super(username, password);
    }
}
