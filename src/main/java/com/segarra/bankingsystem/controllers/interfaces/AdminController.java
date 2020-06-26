package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.FinanceAdminRequest;

public interface AdminController {
    void financeAccount(int id, FinanceAdminRequest financeAdminRequest);
}
