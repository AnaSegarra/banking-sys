package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.*;
import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.exceptions.IllegalTransactionException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Logger LOGGER = LogManager.getLogger(AccountService.class);

    // retrieve any account by id - admin restricted
    @Secured({"ROLE_ADMIN"})
    public AccountVM getById(int id){
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account with id " + id + " not found"));
        if (account instanceof CheckingAccount) {
            // apply fee if necessary
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.applyMonthlyMaintenanceFee();
            checkingAccountRepository.save(checkingAccount);
            LOGGER.info("Admin GET request to retrieve checking account with id " + id);
            return new AccountVM(account.getId(), account.getBalance(), "Checking account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName()  : "No secondary owner assigned",  ((CheckingAccount) account).getStatus().toString());
        }
        if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.applyAnnualInterest();
            savingsAccountRepository.save(savingsAccount);

            LOGGER.info("Admin GET request to retrieve savings account with id " + id);
            return new AccountVM(account.getId(), account.getBalance(),
                    "Savings account", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", ((SavingsAccount) account).getStatus().toString());

        }
        if (account instanceof CreditCard) {
            LOGGER.info("Admin GET request to retrieve credit card with id " + id);
            CreditCard creditCard = (CreditCard) account;
            creditCard.applyMonthlyInterest();
            creditCardRepository.save(creditCard);
            return new AccountVM(account.getId(), account.getBalance(),
                    "Credit card", account.getPrimaryOwner().getName(),
                    account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", "NONE");
        }

        // student account
        LOGGER.info("Admin GET request to retrieve student account with id " + id);
        return new AccountVM(account.getId(), account.getBalance(), "Student account", account.getPrimaryOwner().getName(),
                account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned",
                ((StudentAccount) account).getStatus().toString());

    }

    // retrieve account by id from logged user - user restricted
    @Secured({"ROLE_ACCOUNTHOLDER"})
    public AccountVM getById(int id, User user){
        Account account = accountRepository.findUserAccountById(user.getId(), id);

        if(account instanceof CheckingAccount){
            LOGGER.info("User GET request to retrieve checking account with id " + id);

            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.applyMonthlyMaintenanceFee();
            checkingAccountRepository.save(checkingAccount);

            return new AccountVM(checkingAccount.getId(), checkingAccount.getBalance(), "Checking account", checkingAccount.getPrimaryOwner().getName(),
                    checkingAccount.getSecondaryOwner() != null ? checkingAccount.getSecondaryOwner().getName() : "No secondary owner assigned",
                    checkingAccount.getStatus().toString());
        }
        if(account instanceof SavingsAccount){
            LOGGER.info("User GET request to retrieve savings account with id " + id);

            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.applyAnnualInterest();
            savingsAccountRepository.save(savingsAccount);

            return new AccountVM(savingsAccount.getId(), savingsAccount.getBalance(), "Savings account", savingsAccount.getPrimaryOwner().getName(),
                    savingsAccount.getSecondaryOwner() != null ? savingsAccount.getSecondaryOwner().getName() : "No secondary owner assigned",
                    savingsAccount.getStatus().toString());
        }

        if(account instanceof StudentAccount){
            LOGGER.info("User GET request to retrieve student account with id " + id);

            StudentAccount studentAccount = (StudentAccount) account;

            return new AccountVM(studentAccount.getId(), studentAccount.getBalance(), "Student account", studentAccount.getPrimaryOwner().getName(),
                    studentAccount.getSecondaryOwner() != null ? studentAccount.getSecondaryOwner().getName() : "No secondary owner assigned",
                    studentAccount.getStatus().toString());
        }

        if(account instanceof CreditCard){
            LOGGER.info("User GET request to retrieve credit card with id " + id);

            CreditCard creditCard = (CreditCard) account;
            creditCard.applyMonthlyInterest();
            creditCardRepository.save(creditCard);
            return new AccountVM(creditCard.getId(), creditCard.getBalance(), "Credit card", creditCard.getPrimaryOwner().getName(),
                    creditCard.getSecondaryOwner() != null ? creditCard.getSecondaryOwner().getName() : "No secondary owner assigned",
                    "NONE");
        }

        LOGGER.error("Controlled exception - Account with id " + id + " not found");
        throw new ResourceNotFoundException("Account with id " + id + " not found");
    }

    // create accounts of any type - admin restricted
    @Secured({"ROLE_ADMIN"})
    public Account create(AccountRequest newAccount) {
        if(newAccount.getPrimaryOwnerId() == newAccount.getSecondaryOwnerId()){
            LOGGER.error("Controlled exception - Attempt to create account with same primary and secondary owner");
            throw new IllegalInputException("Primary and secondary owner must be different");
        }
        AccountHolder primaryOwner = accountHolderRepository.findById(newAccount.getPrimaryOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + newAccount.getPrimaryOwnerId() + " not found"));
        AccountHolder secondaryOwner = null;
        if (newAccount.getSecondaryOwnerId() != 0) {
            secondaryOwner = accountHolderRepository.findById(newAccount.getSecondaryOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + newAccount.getSecondaryOwnerId() + " not found"));
        }

        LOGGER.info("Initialized account creation process");
        if((newAccount.getAccountType().equals("savings") || newAccount.getAccountType().equals("checking")) && (newAccount.getSecretKey() == null || !newAccount.getSecretKey().matches("\\d{4}")) ){
            LOGGER.error("Controlled exception - Requested account requires a valid secret key");
            throw new IllegalInputException("Secret key required as a four length number");
        }

        switch (newAccount.getAccountType()) {
            case "savings":
                SavingsAccount savingsAccount = new SavingsAccount(primaryOwner, secondaryOwner,
                        newAccount.getBalance(), newAccount.getSavingsInterestRate(), newAccount.getSecretKey(),
                        newAccount.getSavingsMinimumBalance());

                // prevent creation of account with starting balance under the minimum balance
                if(savingsAccount.getBalance().getAmount().compareTo(savingsAccount.getMinimumBalance()) < 0){
                    LOGGER.error("Controlled exception - Requested account with balance smaller than minimum balance");
                    throw new IllegalInputException("Account balance must be above the minimum balance of " + savingsAccount.getMinimumBalance());
                }

                LOGGER.info("Savings account created");
                return savingsAccountRepository.save(savingsAccount);
            case "checking":
                int age = Period.between(primaryOwner.getBirthday(), LocalDate.now()).getYears();
                if (age < 24) {
                    StudentAccount studentAccount = new StudentAccount(primaryOwner, secondaryOwner, newAccount.getBalance(),
                            newAccount.getSecretKey());
                    LOGGER.info("Student checking account created");
                    return studentAccountRepository.save(studentAccount);
                }
                CheckingAccount checkingAccount = new CheckingAccount(primaryOwner, secondaryOwner, newAccount.getBalance(),
                        newAccount.getSecretKey());

                // prevent creation of account with starting balance under the minimum balance
                if(checkingAccount.getBalance().getAmount().compareTo(checkingAccount.getMinimumBalance()) < 0){
                    LOGGER.error("Controlled exception - Requested account with balance smaller than minimum balance");
                    throw new IllegalInputException("Account balance must be above the minimum balance of " + checkingAccount.getMinimumBalance());
                }
                LOGGER.info("Checking account created");
                return checkingAccountRepository.save(checkingAccount);
            case "credit-card":
                CreditCard creditCard = new CreditCard(primaryOwner, secondaryOwner, newAccount.getBalance(),
                        newAccount.getCreditCardLimit(), newAccount.getCardInterestRate());
                LOGGER.info("Credit card created");
                return creditCardRepository.save(creditCard);
        }
        LOGGER.error("Controlled exception - Not valid account type requested upon creation");
        // throw error if account type doesn't exist
        throw new IllegalInputException("Must enter a valid account type of either savings, checking or credit-card");
    }

    // update account status to ACTIVE - admin restricted
    @Secured({"ROLE_ADMIN"})
    public void unfreezeAccount(int accountId){
        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if(account instanceof CreditCard){
            LOGGER.error("Controlled exception - Id " + accountId + " matches a credit card");
            throw new IllegalInputException("Credit card " + accountId + " doesn't have a status");
        } else if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            if(checkingAccount.getStatus() == Status.ACTIVE){
                LOGGER.error("Controlled exception - Checking account " + accountId + " is already active");
                throw new IllegalInputException("Account " + accountId + " is already active");
            }
            checkingAccount.setStatus(Status.ACTIVE);
            checkingAccountRepository.save(checkingAccount);
            LOGGER.info("Checking account " + accountId + " successfully updated");

        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            if(savingsAccount.getStatus() == Status.ACTIVE){
                LOGGER.error("Controlled exception - Savings account " + accountId + " is already active");
                throw new IllegalInputException("Savings account " + accountId + " is already active");
            }
            savingsAccount.setStatus(Status.ACTIVE);
            savingsAccountRepository.save(savingsAccount);
            LOGGER.info("Savings account " + accountId + " successfully updated");

        } else if (account instanceof StudentAccount) {
            StudentAccount studentAccount = (StudentAccount) account;
            if(studentAccount.getStatus() == Status.ACTIVE){
                LOGGER.error("Controlled exception - Student account " + accountId + " is already active");
                throw new IllegalInputException("Student account " + accountId + " is already active");
            }
            studentAccount.setStatus(Status.ACTIVE);
            studentAccountRepository.save(studentAccount);
            LOGGER.info("Student account " + accountId + " successfully updated");
        }

    }

    public void applyFinance(Account account, String operation, BigDecimal amount){
        if(operation.equals("debit")){
            if(account.getBalance().getAmount().compareTo(amount) < 0){
                LOGGER.error("Controlled exception - Account " + account.getId() + " doesn't have enough funds");
                throw new IllegalInputException("Unable to process this credit: insufficient funds");
            }
            account.getBalance().decreaseAmount(amount);
            LOGGER.info("Processed a debit of " + amount + " on account " + account.getId());
        } else if (operation.equals("credit")){
            account.getBalance().increaseAmount(amount);
            LOGGER.info("Processed a credit of " + amount + " on account " + account.getId());
        } else {
            LOGGER.error("Controlled exception - Not valid operation type requested");
            throw new IllegalInputException("Must enter a valid operation of either debit or credit");
        }
    }

    // debit or credit accounts - admin restricted
    @Secured({"ROLE_ADMIN"})
    public void financeAccount(int accountId, FinanceAdminRequest financeAdminRequest){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account with id " + accountId + " not found"));
        LOGGER.info("Initialized process to finance account " + accountId + " by an admin");

        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkingAccount.applyMonthlyMaintenanceFee();
            applyFinance(checkingAccount, financeAdminRequest.getOperation().toLowerCase(), financeAdminRequest.getAmount());
            checkingAccount.applyPenaltyFee(checkingAccount.getMinimumBalance());
            if(account.getBalance().getAmount().compareTo(checkingAccount.getMinimumBalance()) > 0){
                account.setPenaltyFeeApplied(false);
            }
            checkingAccountRepository.save(checkingAccount);

        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            savingsAccount.applyAnnualInterest();
            applyFinance(savingsAccount, financeAdminRequest.getOperation().toLowerCase(), financeAdminRequest.getAmount());
            savingsAccount.applyPenaltyFee(savingsAccount.getMinimumBalance());
            if(account.getBalance().getAmount().compareTo(savingsAccount.getMinimumBalance()) > 0){
                account.setPenaltyFeeApplied(false);
            }
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

    // debit or credit accounts; account secret key required - third party restricted
    @Secured({"ROLE_THIRDPARTY"})
    public void financeAccount(int accountId, FinanceThirdPartyRequest financeThirdPartyRequestRequest){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Account with id " + accountId + " not found"));
        LOGGER.info("Initialized process to finance account " + accountId + " by a third party");

        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            if(!checkingAccount.getSecretKey().equals(financeThirdPartyRequestRequest.getSecretKey())){
                LOGGER.error("Controlled exception - Access denied to account " + accountId + " as secret key is not correct");
                throw new IllegalInputException("Unable to access this account: wrong secret key");
            }

            checkingAccount.applyMonthlyMaintenanceFee();
            applyFinance(checkingAccount, financeThirdPartyRequestRequest.getOperation().toLowerCase(), financeThirdPartyRequestRequest.getAmount());
            checkingAccount.applyPenaltyFee(checkingAccount.getMinimumBalance());
            if(account.getBalance().getAmount().compareTo(checkingAccount.getMinimumBalance()) > 0){
                account.setPenaltyFeeApplied(false);
            }
            checkingAccountRepository.save(checkingAccount);

        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            if(!savingsAccount.getSecretKey().equals(financeThirdPartyRequestRequest.getSecretKey())){
                LOGGER.error("Controlled exception - Access denied to account " + accountId + " as secret key is not correct");
                throw new IllegalInputException("Unable to access this account: wrong secret key");
            }

            savingsAccount.applyAnnualInterest();
            applyFinance(savingsAccount, financeThirdPartyRequestRequest.getOperation().toLowerCase(), financeThirdPartyRequestRequest.getAmount());
            savingsAccount.applyPenaltyFee(savingsAccount.getMinimumBalance());
            if(account.getBalance().getAmount().compareTo(savingsAccount.getMinimumBalance()) > 0){
                account.setPenaltyFeeApplied(false);
            }
            savingsAccountRepository.save(savingsAccount);

        } else if (account instanceof CreditCard) {
            LOGGER.error("Controlled exception - Access denied to account " + accountId + " given that it does not have a secret key");
            // throws 403 Forbidden as credit cards don't have a secret key
            throw new IllegalTransactionException("Unable to access this account");
        } else if (account instanceof StudentAccount) {
            StudentAccount studentAccount = (StudentAccount) account;
            if(!studentAccount.getSecretKey().equals(financeThirdPartyRequestRequest.getSecretKey())){
                LOGGER.error("Controlled exception - Access denied to account " + accountId + " as secret key is not correct");
                throw new IllegalInputException("Unable to access this account: wrong secret key");
            }
            applyFinance(studentAccount, financeThirdPartyRequestRequest.getOperation().toLowerCase(), financeThirdPartyRequestRequest.getAmount());
            studentAccountRepository.save(studentAccount);
        }
    }

    // retrieve accounts from logged user - user restricted
    @Secured({"ROLE_ACCOUNTHOLDER"})
    public List<AccountVM> getAllUserAccounts(User user){
        List<Account> accounts = accountRepository.findAllUserAccounts(user.getId());
        Stream<AccountVM> checkingAccounts = accounts.stream().filter(account -> account.getClass().getSimpleName().equals("CheckingAccount"))
                .map(account -> new AccountVM(account.getId(), account.getBalance(), "Checking account", account.getPrimaryOwner().getName(),
                account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", ((CheckingAccount) account).getStatus().toString()));
        Stream<AccountVM> savingsAccounts = accounts.stream().filter(account -> account.getClass().getSimpleName().equals("SavingsAccount"))
                .map(account -> new AccountVM(account.getId(), account.getBalance(), "Savings account", account.getPrimaryOwner().getName(),
                        account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", ((SavingsAccount) account).getStatus().toString()));
        Stream<AccountVM> studentAccounts = accounts.stream().filter(account -> account.getClass().getSimpleName().equals("StudentAccount"))
                .map(account -> new AccountVM(account.getId(), account.getBalance(), "Student account", account.getPrimaryOwner().getName(),
                        account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", ((StudentAccount) account).getStatus().toString()));
        Stream<AccountVM> creditCards = accounts.stream().filter(account -> account.getClass().getSimpleName().equals("CreditCard"))
                .map(account -> new AccountVM(account.getId(), account.getBalance(), "Credit card", account.getPrimaryOwner().getName(),
                        account.getSecondaryOwner() != null ? account.getSecondaryOwner().getName() : "No secondary owner assigned", "NONE"));


        LOGGER.info("GET request to retrieve every account from user " + user.getUsername());
        return Stream.of(checkingAccounts, savingsAccounts, studentAccounts, creditCards).flatMap(i -> i)
                .collect(Collectors.toList());
    }
}
