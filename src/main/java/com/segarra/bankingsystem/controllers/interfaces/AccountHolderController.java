package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.Transaction;

import java.util.List;

public interface AccountHolderController {
    List<AccountHolder> getAll();
    AccountHolder create(AccountHolder accountHolder);
    void makeTransaction(String senderType, String recipientType, TransactionRequest transaction);
}
