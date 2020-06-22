package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountBody;
import com.segarra.bankingsystem.models.Account;

public interface AccountController {
    <T extends Account> Account create(String accountType, AccountBody newAccount);
}
