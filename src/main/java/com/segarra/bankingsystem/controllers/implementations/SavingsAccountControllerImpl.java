package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.SavingsAccountController;
import com.segarra.bankingsystem.models.SavingsAccount;
import com.segarra.bankingsystem.services.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SavingsAccountControllerImpl implements SavingsAccountController {
    @Autowired
    private SavingsAccountService savingsAccountService;

    @GetMapping("/savings-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getAll() {
        return savingsAccountService.getAll();
    }
}
