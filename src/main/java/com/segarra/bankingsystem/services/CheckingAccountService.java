package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.repositories.CheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckingAccountService {
    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    public List<CheckingAccount> getAll(){
        return checkingAccountRepository.findAll();
    }
}
