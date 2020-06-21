package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.SavingsAccountBody;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CreditCard;
import com.segarra.bankingsystem.models.SavingsAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountService {
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    public List<SavingsAccount> getAll(){
        return savingsAccountRepository.findAll();
    }

    public SavingsAccount create(SavingsAccountBody newSavingsAccount){
        AccountHolder primaryOwner = accountHolderRepository.findById(newSavingsAccount.getPrimaryOwnerId())
                .orElseThrow(()->new ResourceNotFoundException("Customer with id " + newSavingsAccount.getPrimaryOwnerId() + " not found"));
        AccountHolder secondaryOwner = null;
        if(newSavingsAccount.getSecondaryOwnerId() != 0){
            secondaryOwner = accountHolderRepository.findById(newSavingsAccount.getSecondaryOwnerId())
                    .orElseThrow(()->new ResourceNotFoundException("Customer with id " + newSavingsAccount.getSecondaryOwnerId() + " not found"));
        }
        SavingsAccount savingsAccount = new SavingsAccount(primaryOwner, secondaryOwner,newSavingsAccount.getBalance(),
                newSavingsAccount.getInterestRate(), newSavingsAccount.getSecretKey(),
                newSavingsAccount.getMinimumBalance());

        return savingsAccountRepository.save(savingsAccount);
    }
}
