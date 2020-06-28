package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountHolderController;
import com.segarra.bankingsystem.dto.AccountHolderRequest;
import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody AccountHolder accountHolder) {
        return accountHolderService.create(accountHolder);
    }
}
