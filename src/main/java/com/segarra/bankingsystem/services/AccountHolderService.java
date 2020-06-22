package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.exceptions.InsufficientFundsException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CheckingAccountRepository;
import com.segarra.bankingsystem.repositories.SavingsAccountRepository;
import com.segarra.bankingsystem.repositories.TransactionRepository;
import com.segarra.bankingsystem.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountHolderService {
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public List<AccountHolder> getAll(){
        return accountHolderRepository.findAll();
    }

    public AccountHolder create(AccountHolder accountHolder){
        return accountHolderRepository.save(accountHolder);
    }

    @Transactional
    public void makeTransaction(String recipientType, String senderType, TransactionRequest transaction){
        if(senderType.equals("savings")){
            SavingsAccount senderAccount = savingsAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));

            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new InsufficientFundsException("Unable to make this transfer: insufficient funds");
            }

            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            senderAccount.applyPenaltyFee(senderAccount.getMinimumBalance());
            savingsAccountRepository.save(senderAccount);
        }

        if(recipientType.equals("savings")){
            SavingsAccount recipientAccount = savingsAccountRepository.findById(transaction.getRecipientId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getRecipientId() + " not found"));

            recipientAccount.getBalance().increaseAmount(transaction.getAmount());
            savingsAccountRepository.save(recipientAccount);
        }
    }
}
