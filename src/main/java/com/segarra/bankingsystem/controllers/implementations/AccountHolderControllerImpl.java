package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountHolderController;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountHolderControllerImpl implements AccountHolderController {
    @Autowired
    AccountHolderService accountHolderService;

    @Override
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAll() {
        return accountHolderService.getAll();
    }

    @Override
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder create(@Valid @RequestBody AccountHolder accountHolder) {
        return accountHolderService.create(accountHolder);
    }

}
