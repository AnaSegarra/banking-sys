package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.CheckingAccountBody;
import com.segarra.bankingsystem.dto.CheckingAccountMV;
import com.segarra.bankingsystem.models.CheckingAccount;

import java.util.List;

public interface CheckingAccountController {
    List<CheckingAccount> getAll();
    CheckingAccountMV create(CheckingAccountBody checkingAccount);
}
