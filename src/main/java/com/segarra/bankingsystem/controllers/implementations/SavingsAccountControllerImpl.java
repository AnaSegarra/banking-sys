package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.SavingsAccountController;
import com.segarra.bankingsystem.dto.SavingsAccountBody;
import com.segarra.bankingsystem.models.SavingsAccount;
import com.segarra.bankingsystem.services.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SavingsAccountControllerImpl implements SavingsAccountController {
    @Autowired
    SavingsAccountService savingsAccountService;

    @Override
    @GetMapping("/savings-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getAll() {
        return savingsAccountService.getAll();
    }

    @Override
    @PostMapping("/savings-accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount create(@Valid @RequestBody SavingsAccountBody newSavingsAccount) {
        return savingsAccountService.create(newSavingsAccount);
    }
}
