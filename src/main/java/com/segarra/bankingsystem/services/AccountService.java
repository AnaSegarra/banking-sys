package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.*;
import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.exceptions.IllegalTransactionException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    // retrieve of any account by id - admin restricted
    @Secured({"ROLE_ADMIN"})
    public AccountVM getById(int id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account with id " + id + " not found"));
        if (account instanceof CheckingAccount) {
            return new AccountVM(account.getId(), account.getBalance(), "Checking account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName()  : "No secondary owner assigned",  ((CheckingAccount) account).getStatus().toString());
        }
        if (account instanceof SavingsAccount) {
            return new AccountVM(account.getId(), account.getBalance(),
                    "Savings account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", ((SavingsAccount) account).getStatus().toString());

        }
        if (account instanceof CreditCard) {
            return new AccountVM(account.getId(), account.getBalance(),
                    "Credit card", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned");
        }

        // student account
        return new AccountVM(account.getId(), account.getBalance(), "Student account", account.getPrimaryOwner().getName(),
                account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", ((StudentAccount) account).getStatus().toString());

    }

    // create accounts of any type - admin restricted
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

    @Secured({"ROLE_ADMIN"})
    public void unfreezeAccount(int accountId){
        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("Account with id " + accountId + " not found"));
        if(account instanceof CreditCard){
            throw new IllegalInputException("Credit card " + accountId + " doesn't have a status");
        } else if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            if(checkingAccount.getStatus() == Status.ACTIVE){
                throw new IllegalInputException("Account " + accountId + " is already active");
            }
            checkingAccount.setStatus(Status.ACTIVE);
            checkingAccountRepository.save(checkingAccount);

        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            if(savingsAccount.getStatus() == Status.ACTIVE){
                throw new IllegalInputException("Account " + accountId + " is already active");
            }
            savingsAccount.setStatus(Status.ACTIVE);
            savingsAccountRepository.save(savingsAccount);

        } else if (account instanceof StudentAccount) {
            StudentAccount studentAccount = (StudentAccount) account;
            if(studentAccount.getStatus() == Status.ACTIVE){
                throw new IllegalInputException("Account " + accountId + " is already active");
            }
            studentAccount.setStatus(Status.ACTIVE);
            studentAccountRepository.save(studentAccount);
        }

    }

    public void applyFinance(Account account, String operation, BigDecimal amount){
        if(operation.equals("debit")){
            account.getBalance().decreaseAmount(amount);
        } else if (operation.equals("credit")){
            account.getBalance().increaseAmount(amount);
        } else {
            throw new IllegalInputException("Must enter a valid operation of either debit or credit");
        }
    }


    // debit or credit accounts, account secret key required - third party restricted
    @Secured({"ROLE_THIRDPARTY"})
    public void financeAccount(int accountId, FinanceThirdPartyRequest financeThirdPartyRequestRequest){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            if(checkingAccount.getSecretKey() != financeThirdPartyRequestRequest.getSecretKey()){
                throw new IllegalInputException("Unable to access this account: wrong secret key");
            }

            checkingAccount.applyMonthlyMaintenanceFee();
            applyFinance(checkingAccount, financeThirdPartyRequestRequest.getOperation().toLowerCase(), financeThirdPartyRequestRequest.getAmount());
            checkingAccount.applyPenaltyFee(checkingAccount.getMinimumBalance());
            checkingAccountRepository.save(checkingAccount);

        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            if(savingsAccount.getSecretKey() != financeThirdPartyRequestRequest.getSecretKey()){
                throw new IllegalInputException("Unable to access this account: wrong secret key");
            }

            savingsAccount.applyAnnualInterest();
            applyFinance(savingsAccount, financeThirdPartyRequestRequest.getOperation().toLowerCase(), financeThirdPartyRequestRequest.getAmount());
            savingsAccount.applyPenaltyFee(savingsAccount.getMinimumBalance());
            savingsAccountRepository.save(savingsAccount);

        } else if (account instanceof CreditCard) {
            // throws 403 Forbidden as credit cards don't have a secret key
            throw new IllegalTransactionException("Unable to access this account");
        } else if (account instanceof StudentAccount) {
            StudentAccount studentAccount = (StudentAccount) account;
            if(studentAccount.getSecretKey() != financeThirdPartyRequestRequest.getSecretKey()){
                throw new IllegalInputException("Unable to access this account: wrong secret key");
            }
            applyFinance(studentAccount, financeThirdPartyRequestRequest.getOperation().toLowerCase(), financeThirdPartyRequestRequest.getAmount());
            studentAccountRepository.save(studentAccount);
        }
    }
}
