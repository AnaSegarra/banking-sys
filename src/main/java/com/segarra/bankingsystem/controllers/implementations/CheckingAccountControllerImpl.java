package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.CheckingAccountController;
import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.services.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CheckingAccountControllerImpl implements CheckingAccountController {
    @Autowired
    private CheckingAccountService checkingAccountService;

    @GetMapping("/checking-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> getAll() {
        return checkingAccountService.getAll();
    }

}
