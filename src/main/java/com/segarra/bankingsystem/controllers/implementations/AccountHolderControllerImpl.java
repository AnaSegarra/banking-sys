package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.AccountHolderController;
import com.segarra.bankingsystem.dto.TransactionRequest;
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
    @PostMapping("/users/transaction")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void makeTransaction(@RequestParam("recipient") String recipientType, @RequestParam("sender") String senderType,
                                @RequestBody TransactionRequest transaction, @AuthenticationPrincipal User user) {
        accountHolderService.makeTransaction(recipientType, senderType, transaction, user);
    }
}
