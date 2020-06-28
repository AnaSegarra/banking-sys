package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.dto.FinanceAdminRequest;
import com.segarra.bankingsystem.dto.FinanceThirdPartyRequest;
import com.segarra.bankingsystem.models.Account;
import com.segarra.bankingsystem.models.User;

import java.util.List;

public interface AccountController {
    Account create(AccountRequest newAccount);
    AccountVM getById(int id);
    AccountVM getById(int id, User user);
    List<AccountVM> getAllUserAccounts(User user);
    void financeAccount(int id, FinanceThirdPartyRequest financeAdminRequest);
    void financeAccount(int id, FinanceAdminRequest financeAdminRequest);
    void unfreezeAccount(int id);
}
