package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.dto.FinanceThirdPartyRequest;
import com.segarra.bankingsystem.models.Account;

public interface AccountController {
    Account create(String accountType, AccountRequest newAccount);
    AccountVM getById(int id);
    void financeAccount(int id, FinanceThirdPartyRequest financeAdminRequest);
    void unfreezeAccount(int id);
}
