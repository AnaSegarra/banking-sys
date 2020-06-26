package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountController;
import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.dto.FinanceThirdPartyRequest;
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

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountVM getById(@PathVariable int id) {
        return accountService.getById(id);
    }

    @PatchMapping("/accounts/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfreezeAccount(@PathVariable int id) {
        accountService.unfreezeAccount(id);
    }

    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestParam(name = "type") String accountType,
                          @Valid @RequestBody AccountRequest newAccount) {
        return accountService.create(accountType, newAccount);
    }

    @PostMapping("/third-parties/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void financeAccount(@PathVariable("id") int id, @Valid @RequestBody FinanceThirdPartyRequest financeThirdPartyRequest) {
        accountService.financeAccount(id, financeThirdPartyRequest);
    }
}
