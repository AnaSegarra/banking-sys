package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.models.AccountHolder;

import java.util.List;

public interface AccountHolderController {
    List<AccountHolder> getAll();
    AccountHolder create(AccountHolder accountHolder);
}
