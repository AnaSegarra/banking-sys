package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountController;
import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.models.Account;
import com.segarra.bankingsystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AccountControllerImpl implements AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestParam(name = "type") String accountType,
                          @Valid @RequestBody AccountRequest newAccount) {
        return accountService.create(accountType, newAccount);
    }
}
