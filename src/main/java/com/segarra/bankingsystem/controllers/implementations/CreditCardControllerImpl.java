package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.CreditCardController;
import com.segarra.bankingsystem.models.CreditCard;
import com.segarra.bankingsystem.services.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CreditCardControllerImpl implements CreditCardController {
    @Autowired
    CreditCardService creditCardService;

    @Override
    @GetMapping("/credit-cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> getAll() {
        return creditCardService.getAll();
    }

}
