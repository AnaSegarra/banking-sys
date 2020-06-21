package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.CheckingAccountBody;
import com.segarra.bankingsystem.dto.CheckingAccountMV;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.models.StudentAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CheckingAccountRepository;
import com.segarra.bankingsystem.repositories.StudentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class CheckingAccountService {
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    public List<CheckingAccount> getAll(){
        return checkingAccountRepository.findAll();
    }

    public CheckingAccountMV create(CheckingAccountBody newAccount){
        AccountHolder primaryOwner = accountHolderRepository.findById(newAccount.getPrimaryOwnerId())
                .orElseThrow(()->new ResourceNotFoundException("Customer with id " + newAccount.getPrimaryOwnerId() + " not found"));
        AccountHolder secondaryOwner = null;
        if(newAccount.getSecondaryOwnerId() != 0){
            secondaryOwner = accountHolderRepository.findById(newAccount.getSecondaryOwnerId())
                    .orElseThrow(()->new ResourceNotFoundException("Customer with id " + newAccount.getSecondaryOwnerId() + " not found"));
        }

        int age = Period.between(primaryOwner.getBirthday(), LocalDate.now()).getYears();
        if( age < 24 ){
            StudentAccount studentAccount = new StudentAccount();
            studentAccount.setBalance(newAccount.getBalance());
            studentAccount.setPrimaryOwner(primaryOwner);
            studentAccount.setSecondaryOwner(secondaryOwner);
            studentAccount.setSecretKey(newAccount.getSecretKey());
            studentAccountRepository.save(studentAccount);
            return new CheckingAccountMV(studentAccount);
        }
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setBalance(newAccount.getBalance());
        checkingAccount.setPrimaryOwner(primaryOwner);
        checkingAccount.setSecondaryOwner(secondaryOwner);
        checkingAccount.setSecretKey(newAccount.getSecretKey());

        checkingAccountRepository.save(checkingAccount);
        return new CheckingAccountMV(checkingAccount);
    }
}
