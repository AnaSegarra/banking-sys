package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.FinanceAdminRequest;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;

    public void applyFinance(Account account, String operation, BigDecimal amount){
        if(operation.equals("debit")){
            account.getBalance().decreaseAmount(amount);
        } else if (operation.equals("credit")){
            account.getBalance().increaseAmount(amount);
        } else {
            throw new IllegalInputException("Must enter a valid operation of either debit or credit");
        }
    }

    @Secured({"ROLE_ADMIN"})
    public void financeAccount(int accountId, FinanceAdminRequest financeAdminRequest){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.applyMonthlyMaintenanceFee();
            applyFinance(checkingAccount, financeAdminRequest.getOperation().toLowerCase(), financeAdminRequest.getAmount());
            checkingAccount.applyPenaltyFee(checkingAccount.getMinimumBalance());
            checkingAccountRepository.save(checkingAccount);

        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.applyAnnualInterest();
            applyFinance(savingsAccount, financeAdminRequest.getOperation().toLowerCase(), financeAdminRequest.getAmount());
            savingsAccount.applyPenaltyFee(savingsAccount.getMinimumBalance());
            savingsAccountRepository.save(savingsAccount);

        } else if (account instanceof CreditCard) {
            CreditCard creditCard = (CreditCard) account;
            creditCard.applyMonthlyInterest();
            applyFinance(creditCard, financeAdminRequest.getOperation().toLowerCase(), financeAdminRequest.getAmount());
            creditCardRepository.save(creditCard);

        } else if (account instanceof StudentAccount) {
            StudentAccount studentAccount = (StudentAccount) account;
            applyFinance(studentAccount, financeAdminRequest.getOperation().toLowerCase(), financeAdminRequest.getAmount());
            studentAccountRepository.save(studentAccount);
        }
    }
}