package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AuthController;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AuthControllerImpl implements AuthController {
    @Autowired
    AuthService authService;

    @Override
    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder create(@Valid @RequestBody AccountHolder accountHolder) {
        return authService.create(accountHolder);
    }
}
