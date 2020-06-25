package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.models.Account;

public interface AccountController {
    Account create(String accountType, AccountRequest newAccount);
}
