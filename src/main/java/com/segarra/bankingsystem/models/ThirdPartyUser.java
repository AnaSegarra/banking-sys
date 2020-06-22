package com.segarra.bankingsystem.models;

import javax.persistence.*;

@Entity
@Table(name = "third_parties")
public class ThirdPartyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String hashedKey;

    public ThirdPartyUser() {
    }

    public ThirdPartyUser(String name, String hashedKey) {
        this.name = name;
        this.hashedKey = hashedKey;
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

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
