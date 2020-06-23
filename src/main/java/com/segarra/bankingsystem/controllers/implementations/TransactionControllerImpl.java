package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.TransactionController;
import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionControllerImpl implements TransactionController {
    @Autowired
    TransactionService transactionService;

    @Override
    @PostMapping("/users/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void makeTransaction(@RequestParam("recipient") String recipientType, @RequestParam("sender") String senderType,
                                @RequestBody TransactionRequest transaction, @AuthenticationPrincipal User user) {
        transactionService.makeTransaction(recipientType, senderType, transaction, user);
    }
}
