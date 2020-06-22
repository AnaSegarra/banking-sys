package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountBody;
import com.segarra.bankingsystem.models.Account;

public interface AccountController {
    Account create(String accountType, AccountBody newAccount);
}
