package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.User;

public interface TransactionController {
    void makeTransaction(TransactionRequest transaction, User user);
}
