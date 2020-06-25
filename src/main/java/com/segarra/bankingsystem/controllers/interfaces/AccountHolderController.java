package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.User;

import java.util.List;

public interface AccountHolderController {
    List<AccountHolder> getAll();
    AccountHolder create(AccountHolder accountHolder);
    List<AccountVM> getAllAccounts(User user);
}
