package com.segarra.bankingsystem.models;

import javax.persistence.*;

@Entity
public class ThirdPartyUser extends User{
    public ThirdPartyUser() {
    }

    public ThirdPartyUser(String username, String password) {
        super(username, password);
    }
}
