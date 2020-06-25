package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AdminController;
import com.segarra.bankingsystem.dto.DebitCreditRequest;
import com.segarra.bankingsystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AdminControllerImpl implements AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/accounts/{id}")
    public void financeAccount(@PathVariable("id") int id, @RequestBody DebitCreditRequest debitCreditRequest) {
        adminService.financeAccount(id, debitCreditRequest);
    }

}
