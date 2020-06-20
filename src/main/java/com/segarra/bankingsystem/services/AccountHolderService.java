package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHolderService {
    @Autowired
    AccountHolderRepository accountHolderRepository;

    public List<AccountHolder> getAll(){
        return accountHolderRepository.findAll();
    }

    public AccountHolder create(AccountHolder accountHolder){
        return accountHolderRepository.save(accountHolder);
    }
}
