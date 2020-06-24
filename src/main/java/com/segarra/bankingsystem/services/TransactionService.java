package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.exceptions.*;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TransactionService {
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public boolean checkFraud(Account account, LocalDateTime date, BigDecimal transaction){
        BigDecimal highest = transactionRepository.findHighestDailyTransactionByCustomer(date, account);
        BigDecimal currentTotal = transactionRepository.findTodayTotalTransactions(date, account);
        LocalDateTime lastTransaction =  transactionRepository.findLastTransaction(account);
        if(currentTotal == null || lastTransaction == null){
            return false;
        }

        /* freeze account in case today's transactions add to more than 150% of the highest daily total
           transactions any other day
        */
        if(highest.multiply(new BigDecimal("2.5")).compareTo(currentTotal.add(transaction)) < 0){
            return true;
        }

        // freeze account if two transactions happened in less than a second
        int seconds = (int) lastTransaction.until(date, ChronoUnit.SECONDS);
        // set to one after testing
        return seconds <= 10;
    }

    @PreAuthorize("authenticated")
    @Transactional(dontRollbackOn = FrozenAccountException.class)
    public void makeTransaction(String recipientType, String senderType, TransactionRequest transaction, User user){
        // throw error if accounts ids are the same
        if(transaction.getRecipientId() == transaction.getSenderId())
            throw new IllegalInputException("Recipient account must be different from sender account");

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        // SENDER
        if(senderType.equals("savings")){
            SavingsAccount senderAccount = savingsAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername()) && senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                throw new IllegalTransactionException("Unable to access this account");
            }
            if(senderAccount.getStatus().equals(Status.FROZEN)){
                throw new IllegalInputException("Unable to make this transaction: account with id " + senderAccount.getId() + " is frozen");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }

            if(checkFraud(senderAccount, newTransaction.getDate(), transaction.getAmount())){
                senderAccount.setStatus(Status.FROZEN);
                savingsAccountRepository.save(senderAccount);
                throw new FrozenAccountException("Suspicious activity detected: the account has been frozen");
            };
            senderAccount.applyAnnualInterest();
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            senderAccount.applyPenaltyFee(senderAccount.getMinimumBalance());
            savingsAccountRepository.save(senderAccount);
        } else if(senderType.equals("checking")){
            CheckingAccount senderAccount = checkingAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername()) && senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                throw new IllegalTransactionException("Unable to access this account");
            }
            if(senderAccount.getStatus().equals(Status.FROZEN)){
                throw new IllegalInputException("Unable to make this transaction: account with id " + senderAccount.getId() + " is frozen");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }
            if(checkFraud(senderAccount, newTransaction.getDate(), transaction.getAmount())){
                senderAccount.setStatus(Status.FROZEN);
                checkingAccountRepository.save(senderAccount);
                throw new FrozenAccountException("Suspicious activity detected: the account has been frozen");
            };
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            senderAccount.applyPenaltyFee(senderAccount.getMinimumBalance());
            checkingAccountRepository.save(senderAccount);
        } else if(senderType.equals("student")){
            StudentAccount senderAccount = studentAccountRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername()) && senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                throw new IllegalTransactionException("Unable to access this account");
            }
            if(senderAccount.getStatus().equals(Status.FROZEN)){
                throw new IllegalInputException("Unable to make this transaction: account with id " + senderAccount.getId() + " is frozen");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }

            if(checkFraud(senderAccount, newTransaction.getDate(), transaction.getAmount())){
                senderAccount.setStatus(Status.FROZEN);
                studentAccountRepository.save(senderAccount);
                throw new FrozenAccountException("Suspicious activity detected: the account has been frozen");
            };
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            studentAccountRepository.save(senderAccount);
        } else if(senderType.equals("credit-card")){
            CreditCard senderAccount = creditCardRepository.findById(transaction.getSenderId())
                    .orElseThrow(()-> new ResourceNotFoundException("Account with id " + transaction.getSenderId() + " not found"));
            if(!senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername()) && senderAccount.getSecondaryOwner() != null && !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                throw new IllegalTransactionException("Unable to access this account");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }
            senderAccount.applyMonthlyInterest();
            newTransaction.setSenderId(senderAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            creditCardRepository.save(senderAccount);
        } else {
            throw new IllegalInputException("Must enter a valid account type of either savings, checking, student or credit-card");
        }

        // RECIPIENT
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
            throw new IllegalInputException("Must enter a valid account type of either savings, checking, student or credit-card");
        }
        transactionRepository.save(newTransaction);
    }
}
