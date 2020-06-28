package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountHolderRequest;
import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.models.AccountHolder;

import java.util.List;

public interface AccountHolderController {
    List<AccountHolderVM> getAll();
    AccountHolderVM create(AccountHolderRequest accountHolder);
}
