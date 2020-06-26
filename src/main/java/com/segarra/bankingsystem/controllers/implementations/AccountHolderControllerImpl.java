package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountHolderController;
import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AccountHolderControllerImpl implements AccountHolderController {
    @Autowired
    private AccountHolderService accountHolderService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolderVM> getAll() {
        return accountHolderService.getAllAccountHolders();
    }

    @GetMapping("/users/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountVM> getAllAccounts(@AuthenticationPrincipal User user){
        return accountHolderService.getAllAccounts(user);
    }

    @GetMapping("/users/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountVM getAccountById(@PathVariable int id, @AuthenticationPrincipal User user){
        return accountHolderService.getAccountById(id, user);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder create(@Valid @RequestBody AccountHolder accountHolder) {
        return accountHolderService.create(accountHolder);
    }
}
