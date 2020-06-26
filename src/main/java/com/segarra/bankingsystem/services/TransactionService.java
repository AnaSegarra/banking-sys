package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.exceptions.*;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private static final Logger LOGGER = LogManager.getLogger(TransactionService.class);


    public boolean checkFraud(Account account, LocalDateTime date, BigDecimal transaction){
        BigDecimal highest = transactionRepository.findHighestDailyTransactionByCustomer(date, account);
        BigDecimal currentTotal = transactionRepository.findTodayTotalTransactions(date, account);
        LocalDateTime lastTransaction =  transactionRepository.findLastTransaction(account);
        /* freeze account in case today's transactions add to more than 150% of the highest daily total
           transactions any other day
        */
        if(highest != null && highest.multiply(new BigDecimal("2.5")).compareTo(currentTotal.add(transaction)) < 0){
            return true;
        }

        // don't freeze account if is the first transaction ever
        if(lastTransaction == null){
            return false;
        }

        // freeze account if two transactions happened in less than a second
        int seconds = (int) lastTransaction.until(date, ChronoUnit.SECONDS);
        // set to one after testing
        return seconds <= 10;
    }

    @Secured({"ROLE_ACCOUNTHOLDER"})
    @Transactional(dontRollbackOn = FrozenAccountException.class)
    public void makeTransaction(TransactionRequest transaction, User user){
        // throw error if accounts ids are the same
        if(transaction.getRecipientId() == transaction.getSenderId()){
            throw new IllegalInputException("Recipient account must be different from sender account");
        }
        Account senderAccount = accountRepository.findById(transaction.getSenderId())
                .orElseThrow(()-> new ResourceNotFoundException("Sender account with id " + transaction.getSenderId() + " not found"));
        Account recipientAccount = accountRepository.findById(transaction.getRecipientId())
                .orElseThrow(()-> new ResourceNotFoundException("Recipient account with id " + transaction.getRecipientId() + " not found"));

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        // SENDER
        if (senderAccount instanceof SavingsAccount) {
            // check ownership
            if(!senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername())){
                if(senderAccount.getSecondaryOwner() == null || !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                    LOGGER.error("Controlled exception - " + user.getUsername() + " tried to access an account not owned by them");
                    throw new IllegalTransactionException("Unable to access this account"); // throws 403 Forbidden
                }
            }
            SavingsAccount savingsAccount = (SavingsAccount) senderAccount;
            if(savingsAccount.getStatus().equals(Status.FROZEN)){
                throw new IllegalInputException("Unable to make this transaction: account with id " + senderAccount.getId() + " is frozen");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }

            if(checkFraud(savingsAccount, newTransaction.getDate(), transaction.getAmount())){
                savingsAccount.setStatus(Status.FROZEN);
                savingsAccountRepository.save(savingsAccount);
                throw new FrozenAccountException("Suspicious activity detected: the account has been frozen");
            };
            savingsAccount.applyAnnualInterest();
            newTransaction.setSenderId(savingsAccount);
            savingsAccount.getBalance().decreaseAmount(transaction.getAmount());
            savingsAccount.applyPenaltyFee(savingsAccount.getMinimumBalance());
            savingsAccountRepository.save(savingsAccount);

        } else if (senderAccount instanceof CheckingAccount) {
            // check ownership
            if( !senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername())){
                if(senderAccount.getSecondaryOwner() == null || !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                    throw new IllegalTransactionException("Unable to access this account");
                }
            }

            CheckingAccount checkingAccount = (CheckingAccount) senderAccount;
            if(checkingAccount.getStatus().equals(Status.FROZEN)){
                throw new IllegalInputException("Unable to make this transaction: account with id " + senderAccount.getId() + " is frozen");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }
            if(checkFraud(checkingAccount, newTransaction.getDate(), transaction.getAmount())){
                checkingAccount.setStatus(Status.FROZEN);
                checkingAccountRepository.save(checkingAccount);
                throw new FrozenAccountException("Suspicious activity detected: the account has been frozen");
            };

            newTransaction.setSenderId(checkingAccount);
            checkingAccount.getBalance().decreaseAmount(transaction.getAmount());
            checkingAccount.applyPenaltyFee(checkingAccount.getMinimumBalance());
            checkingAccount.applyMonthlyMaintenanceFee();
            checkingAccountRepository.save(checkingAccount);

        } else if (senderAccount instanceof StudentAccount) {
            // check ownership
            if( !senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername())){
                if(senderAccount.getSecondaryOwner() == null || !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                    throw new IllegalTransactionException("Unable to access this account");
                }
            }

            StudentAccount studentAccount = (StudentAccount) senderAccount;
            if(studentAccount.getStatus().equals(Status.FROZEN)){
                throw new IllegalInputException("Unable to make this transaction: account with id " + senderAccount.getId() + " is frozen");
            }
            if(senderAccount.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }

            if(checkFraud(studentAccount, newTransaction.getDate(), transaction.getAmount())){
                studentAccount.setStatus(Status.FROZEN);
                studentAccountRepository.save(studentAccount);
                throw new FrozenAccountException("Suspicious activity detected: the account has been frozen");
            };
            newTransaction.setSenderId(studentAccount);
            senderAccount.getBalance().decreaseAmount(transaction.getAmount());
            studentAccountRepository.save(studentAccount);

        } else if (senderAccount instanceof CreditCard) {
            // check ownership
            if( !senderAccount.getPrimaryOwner().getUsername().equals(user.getUsername())){
                if(senderAccount.getSecondaryOwner() == null || !senderAccount.getSecondaryOwner().getUsername().equals(user.getUsername())){
                    throw new IllegalTransactionException("Unable to access this account");
                }
            }

            CreditCard creditCard = (CreditCard) senderAccount;
            if(creditCard.getBalance().getAmount().compareTo(transaction.getAmount()) < 0){
                throw new IllegalInputException("Unable to make this transfer: insufficient funds");
            }
            creditCard.applyMonthlyInterest();
            newTransaction.setSenderId(creditCard);
            creditCard.getBalance().decreaseAmount(transaction.getAmount());
            creditCardRepository.save(creditCard);

        } else {
            throw new IllegalInputException("Must enter a valid account type of either savings, checking, student or credit-card");
        }

        // RECIPIENT
        if (recipientAccount instanceof SavingsAccount) {
            // verify recipient's ownership
            if(!transaction.getRecipientName().equals(recipientAccount.getPrimaryOwner().getName())) {
                if (recipientAccount.getSecondaryOwner() == null || !recipientAccount.getSecondaryOwner().getUsername().equals(user.getUsername())) {
                    System.out.println("");
                    throw new IllegalInputException(transaction.getRecipientName() + " is neither the owner or co-owner of the account " + transaction.getRecipientId());
                }
            }

            SavingsAccount savingsAccount = (SavingsAccount) recipientAccount;
            newTransaction.setRecipientId(savingsAccount);
            savingsAccount.getBalance().increaseAmount(transaction.getAmount());
            if(recipientAccount.getBalance().getAmount().compareTo(savingsAccount.getMinimumBalance()) > 0){
                // reset penalty fee applied status if balance surpasses min balance
                recipientAccount.setPenaltyFeeApplied(false);
            }
            savingsAccountRepository.save(savingsAccount);

        } else if (recipientAccount instanceof CheckingAccount) {
            // verify recipient's ownership
            if(!transaction.getRecipientName().equals(recipientAccount.getPrimaryOwner().getName())) {
                if (recipientAccount.getSecondaryOwner() == null || !recipientAccount.getSecondaryOwner().getUsername().equals(user.getUsername())) {
                    throw new IllegalInputException(transaction.getRecipientName() + " is neither the owner or co-owner of the account " + transaction.getRecipientId());
                }
            }

            CheckingAccount checkingAccount = (CheckingAccount) recipientAccount;
            newTransaction.setRecipientId(checkingAccount);
            checkingAccount.getBalance().increaseAmount(transaction.getAmount());
            if(checkingAccount.getBalance().getAmount().compareTo(checkingAccount.getMinimumBalance()) > 0){
                recipientAccount.setPenaltyFeeApplied(false);
            }
            checkingAccountRepository.save(checkingAccount);

        } else if(recipientAccount instanceof StudentAccount){
            // verify recipient's ownership
            if(!transaction.getRecipientName().equals(recipientAccount.getPrimaryOwner().getName())) {
                if (recipientAccount.getSecondaryOwner() == null || !recipientAccount.getSecondaryOwner().getUsername().equals(user.getUsername())) {
                    throw new IllegalInputException(transaction.getRecipientName() + " is neither the owner or co-owner of the account " + transaction.getRecipientId());
                }
            }

            StudentAccount studentAccount = (StudentAccount) recipientAccount;
            newTransaction.setRecipientId(studentAccount);
            studentAccount.getBalance().increaseAmount(transaction.getAmount());
            studentAccountRepository.save(studentAccount);

        } else if(recipientAccount instanceof CreditCard){
            // verify recipient's ownership
            if(!transaction.getRecipientName().equals(recipientAccount.getPrimaryOwner().getName())) {
                if (recipientAccount.getSecondaryOwner() == null || !recipientAccount.getSecondaryOwner().getUsername().equals(user.getUsername())) {
                    throw new IllegalInputException(transaction.getRecipientName() + " is neither the owner or co-owner of the account " + transaction.getRecipientId());
                }
            }

            CreditCard creditCard = (CreditCard) recipientAccount;
            newTransaction.setRecipientId(creditCard);
            creditCard.getBalance().increaseAmount(transaction.getAmount());
            creditCardRepository.save(creditCard);
            
        } else {
            throw new IllegalInputException("Must enter a valid account type of either savings, checking, student or credit-card");
        }
        transactionRepository.save(newTransaction);
    }
}
