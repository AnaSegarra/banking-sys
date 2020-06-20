package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.CheckingAccountController;
import com.segarra.bankingsystem.dto.CheckingAccountBody;
import com.segarra.bankingsystem.dto.CheckingAccountMV;
import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.services.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CheckingAccountImpl implements CheckingAccountController {
    @Autowired
    CheckingAccountService checkingAccountService;

    @Override
    @GetMapping("/checking-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> getAll() {
        return checkingAccountService.getAll();
    }

    @Override
    @PostMapping("/checking-accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingAccountMV create(@Valid @RequestBody CheckingAccountBody checkingAccount) {
        return checkingAccountService.create(checkingAccount);
    }
}
