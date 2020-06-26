package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.User;

import java.util.List;

public interface AccountHolderController {
    List<AccountHolderVM> getAll();
    AccountHolder create(AccountHolder accountHolder);
    AccountVM getAccountById(int id, User user);
}
