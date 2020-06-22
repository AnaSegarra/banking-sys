package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountBody;
import com.segarra.bankingsystem.exceptions.IllegalAccountTypeException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AccountService {
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    public Account create(String accountType, AccountBody newAccount) {
        AccountHolder primaryOwner = accountHolderRepository.findById(newAccount.getPrimaryOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + newAccount.getPrimaryOwnerId() + " not found"));
        AccountHolder secondaryOwner = null;
        if (newAccount.getSecondaryOwnerId() != 0) {
            secondaryOwner = accountHolderRepository.findById(newAccount.getSecondaryOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + newAccount.getSecondaryOwnerId() + " not found"));
        }

        if (accountType.equals("savings")) {
            SavingsAccount savingsAccount = new SavingsAccount(primaryOwner, secondaryOwner,
                    newAccount.getBalance(), newAccount.getSavingsInterestRate(), newAccount.getSecretKey(),
                    newAccount.getSavingsMinimumBalance());
            return savingsAccountRepository.save(savingsAccount);
        } else if (accountType.equals("checking")) {
            int age = Period.between(primaryOwner.getBirthday(), LocalDate.now()).getYears();
            if( age < 24 ){
                StudentAccount studentAccount = new StudentAccount(primaryOwner, secondaryOwner, newAccount.getBalance(),
                        newAccount.getSecretKey());
                return studentAccountRepository.save(studentAccount);
            }
            CheckingAccount checkingAccount = new CheckingAccount(primaryOwner, secondaryOwner, newAccount.getBalance(),
                    newAccount.getSecretKey());
            return checkingAccountRepository.save(checkingAccount);
        } else if(accountType.equals("credit-card")) {
            CreditCard creditCard = new CreditCard(primaryOwner, secondaryOwner, newAccount.getBalance(),
                    newAccount.getCreditCardLimit(), newAccount.getCardInterestRate());
            return creditCardRepository.save(creditCard);
        }

        throw new IllegalAccountTypeException("Must enter a valid account type of either savings, checking or credit-card");
    }
}
