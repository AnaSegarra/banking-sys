package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountController;
import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.dto.FinanceAdminRequest;
import com.segarra.bankingsystem.dto.FinanceThirdPartyRequest;
import com.segarra.bankingsystem.models.Account;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public Account create(@RequestBody @Valid AccountRequest newAccount) {
        return accountService.create(newAccount);
    }

    @PostMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void financeAccount(@PathVariable("id") int id, @RequestBody FinanceAdminRequest financeAdminRequest) {
        accountService.financeAccount(id, financeAdminRequest);
    }

    @GetMapping("/users/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountVM> getAllUserAccounts(@AuthenticationPrincipal User user){
        return accountService.getAllUserAccounts(user);
    }

    @GetMapping("/users/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountVM getById(@PathVariable int id, @AuthenticationPrincipal User user){
        return accountService.getById(id, user);
    }

    @PostMapping("/third-parties/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void financeAccount(@PathVariable("id") int id, @RequestBody @Valid FinanceThirdPartyRequest financeThirdPartyRequest) {
        accountService.financeAccount(id, financeThirdPartyRequest);
    }
}
