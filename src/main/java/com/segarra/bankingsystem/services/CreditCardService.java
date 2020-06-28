package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.CreditCard;
import com.segarra.bankingsystem.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Secured({"ROLE_ADMIN"})
    public List<CreditCard> getAll(){
        return creditCardRepository.findAll();
    }
}
