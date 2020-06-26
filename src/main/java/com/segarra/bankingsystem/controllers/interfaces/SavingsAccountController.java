package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.models.SavingsAccount;

import java.util.List;

public interface SavingsAccountController {
    List<SavingsAccount> getAll();
}
