package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AccountService {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Secured({"ROLE_ADMIN"})
    public AccountVM getById(int id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account with id " + id + " not found"));
        if (account instanceof CheckingAccount) {
            return new AccountVM(account.getId(), account.getBalance(), "Checking account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName()  : "No secondary owner assigned");
        }
        if (account instanceof SavingsAccount) {
            return new AccountVM(account.getId(), account.getBalance(),
                    "Savings account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned");

        }
        if (account instanceof CreditCard) {
            return new AccountVM(account.getId(), account.getBalance(),
                    "Credit card", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned");
        }

        if (account instanceof StudentAccount) {
            return new AccountVM(account.getId(), account.getBalance(),
                    "Student account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned");
        }
        throw new ResourceNotFoundException("Account with id " + id + " not found");
    }

    @Secured({"ROLE_ADMIN"})
    public Account create(String accountType, AccountRequest newAccount) {
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
        // throw error if account type doesn't exist
        throw new IllegalInputException("Must enter a valid account type of either savings, checking or credit-card");
    }
}
