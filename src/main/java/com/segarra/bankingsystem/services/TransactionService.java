package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.exceptions.IllegalAccountTypeException;
import com.segarra.bankingsystem.exceptions.InsufficientFundsException;
import com.segarra.bankingsystem.exceptions.NotOwnerException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransactionService {
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    StudentAccountRepository studentAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    @Secured({"ROLE_ACCOUNTHOLDER"})
    public void makeTransaction(String recipientType, String senderType, TransactionRequest transaction, User user){
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        if(senderType.equals("savings")){
            SavingsAccount senderAccount = savingsAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getName().equals(user.getUsername()) || senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getName().equals(user.getUsername())){
                throw new NotOwnerException("Unable to access this account");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new InsufficientFundsException("Unable to make this transfer: insufficient funds");
            }
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            senderAccount.applyPenaltyFee(senderAccount.getMinimumBalance());
            savingsAccountRepository.save(senderAccount);
        } else if(senderType.equals("checking")){
            CheckingAccount senderAccount = checkingAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getName().equals(user.getUsername()) || senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getName().equals(user.getUsername())){
                throw new NotOwnerException("Unable to access this account");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new InsufficientFundsException("Unable to make this transfer: insufficient funds");
            }
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            senderAccount.applyPenaltyFee(senderAccount.getMinimumBalance());
            checkingAccountRepository.save(senderAccount);
        } else if(senderType.equals("student")){
            StudentAccount senderAccount = studentAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getName().equals(user.getUsername()) || senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getName().equals(user.getUsername())){
                throw new NotOwnerException("Unable to access this account");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new InsufficientFundsException("Unable to make this transfer: insufficient funds");
            }
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            studentAccountRepository.save(senderAccount);
        } else if(senderType.equals("credit-card")){
            CreditCard senderAccount = creditCardRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getName().equals(user.getUsername()) || senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getName().equals(user.getUsername())){
                throw new NotOwnerException("Unable to access this account");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new InsufficientFundsException("Unable to make this transfer: insufficient funds");
            }
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            creditCardRepository.save(senderAccount);
        } else {
            throw new IllegalAccountTypeException("Must enter a valid account type of either savings, checking, student or credit-card");
        }

        if(recipientType.equals("savings")){
            SavingsAccount recipientAccount = savingsAccountRepository.findById(transaction.getRecipientId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getRecipientId() + " not found"));
            newTransaction.setRecipientId(recipientAccount);
            recipientAccount.getBalance().increaseAmount(transaction.getAmount());
            if(recipientAccount.getBalance().getAmount().compareTo(recipientAccount.getMinimumBalance()) > 0){
                // reset penalty fee applied status if balance surpasses min balance
                recipientAccount.setPenaltyFeeApplied(false);
            }
            savingsAccountRepository.save(recipientAccount);
        } else if (recipientType.equals("checking")){
            CheckingAccount recipientAccount = checkingAccountRepository.findById(transaction.getRecipientId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getRecipientId() + " not found"));
            newTransaction.setRecipientId(recipientAccount);
            recipientAccount.getBalance().increaseAmount(transaction.getAmount());
            if(recipientAccount.getBalance().getAmount().compareTo(recipientAccount.getMinimumBalance()) > 0){
                recipientAccount.setPenaltyFeeApplied(false);
            }
            checkingAccountRepository.save(recipientAccount);
        } else if(recipientType.equals("student")){
            StudentAccount recipientAccount = studentAccountRepository.findById(transaction.getRecipientId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getRecipientId() + " not found"));
            newTransaction.setRecipientId(recipientAccount);
            recipientAccount.getBalance().increaseAmount(transaction.getAmount());
            studentAccountRepository.save(recipientAccount);
        } else if(recipientType.equals("credit-card")){
            CreditCard recipientAccount = creditCardRepository.findById(transaction.getRecipientId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getRecipientId() + " not found"));
            newTransaction.setRecipientId(recipientAccount);
            recipientAccount.getBalance().increaseAmount(transaction.getAmount());
            creditCardRepository.save(recipientAccount);
        } else {
            throw new IllegalAccountTypeException("Must enter a valid account type of either savings, checking, student or credit-card");
        }
        transactionRepository.save(newTransaction);
    }
}
