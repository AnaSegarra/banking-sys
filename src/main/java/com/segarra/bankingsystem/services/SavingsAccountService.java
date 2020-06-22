package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.SavingsAccount;
import com.segarra.bankingsystem.repositories.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountService {
    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    public List<SavingsAccount> getAll(){
        return savingsAccountRepository.findAll();
    }
}
