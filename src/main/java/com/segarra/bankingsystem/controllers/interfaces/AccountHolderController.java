package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.User;

import java.util.List;

public interface AccountHolderController {
    List<AccountHolderVM> getAll();
    User create(AccountHolder accountHolder);
}
