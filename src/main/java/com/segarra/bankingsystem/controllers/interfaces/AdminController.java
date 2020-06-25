package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.DebitCreditRequest;

public interface AdminController {
    void financeAccount(int id, DebitCreditRequest debitCreditRequest);
}
