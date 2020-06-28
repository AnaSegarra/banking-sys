package com.segarra.bankingsystem.dto;

public class ThirdPartyUserVM {
    private int id;
    private String username;

    public ThirdPartyUserVM() {
    }

    public ThirdPartyUserVM(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
