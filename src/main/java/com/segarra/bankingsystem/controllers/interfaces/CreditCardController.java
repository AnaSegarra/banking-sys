package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.CreditCardBody;
import com.segarra.bankingsystem.models.CreditCard;

import java.util.List;

public interface CreditCardController {
    List<CreditCard> getAll();
    CreditCard create(CreditCardBody newCard);
}
