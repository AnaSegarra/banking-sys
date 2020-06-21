package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.CreditCardBody;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CreditCard;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    public List<CreditCard> getAll(){
        return creditCardRepository.findAll();
    }

    public CreditCard create(CreditCardBody newCard){
        AccountHolder primaryOwner = accountHolderRepository.findById(newCard.getPrimaryOwnerId())
                .orElseThrow(()->new ResourceNotFoundException("Customer with id " + newCard.getPrimaryOwnerId() + " not found"));
        AccountHolder secondaryOwner = null;
        if(newCard.getSecondaryOwnerId() != 0){
            secondaryOwner = accountHolderRepository.findById(newCard.getSecondaryOwnerId())
                    .orElseThrow(()->new ResourceNotFoundException("Customer with id " + newCard.getSecondaryOwnerId() + " not found"));
        }
        CreditCard creditCard = new CreditCard(primaryOwner, secondaryOwner, newCard.getBalance(), newCard.getCreditLimit(), newCard.getInterestRate());

        return creditCardRepository.save(creditCard);
    }
}
