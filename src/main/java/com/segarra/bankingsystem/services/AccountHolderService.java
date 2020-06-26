package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.exceptions.ResourceNotFoundException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountHolderService {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
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

    private static final Logger LOGGER = LogManager.getLogger(AccountHolderService.class);

    // retrieve every account holder - admin restricted
    @Secured({"ROLE_ADMIN"})
    public List<AccountHolderVM> getAllAccountHolders(){
        return accountHolderRepository.findAll().stream()
                .map(accountHolder -> new AccountHolderVM(accountHolder.getId(), accountHolder.getName(),
                        accountHolder.getUsername(), accountHolder.getBirthday(), accountHolder.getPrimaryAddress(),
                        accountHolder.getMailingAddress())).collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN"})
    public AccountHolder create(AccountHolder accountHolder){
        User foundUser = userRepository.findByUsername(accountHolder.getUsername());
        if(foundUser != null){
            LOGGER.error("Controlled exception - Username " + accountHolder.getUsername() + " is already taken");
            throw new IllegalInputException("This username has already been taken");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword()));
        AccountHolder newUser = accountHolderRepository.save(accountHolder);
        Role role = new Role("ROLE_ACCOUNTHOLDER", newUser);
        roleRepository.save(role);
        LOGGER.info("Account holder created " + newUser);
        return newUser;
    }


    @PreAuthorize("authenticated")
    public AccountVM getAccountById(int id, User user){

        CheckingAccount checkingAccount = checkingAccountRepository.findAccountById(user.getId(), id);
        if(checkingAccount != null){
            checkingAccount.applyMonthlyMaintenanceFee();
            checkingAccountRepository.save(checkingAccount);

            return new AccountVM(checkingAccount.getId(), checkingAccount.getBalance(), checkingAccount.getClass().getSimpleName(), checkingAccount.getPrimaryOwner().getName(),
                    checkingAccount.getSecondaryOwner() != null ? checkingAccount.getSecondaryOwner().getName() : "No secondary owner assigned");
        }

        SavingsAccount savingsAccount = savingsAccountRepository.findAccountById(user.getId(), id);
        if(savingsAccount != null){
            savingsAccount.applyAnnualInterest();
            savingsAccountRepository.save(savingsAccount);

            return new AccountVM(savingsAccount.getId(), savingsAccount.getBalance(), savingsAccount.getClass().getSimpleName(), savingsAccount.getPrimaryOwner().getName(),
                    savingsAccount.getSecondaryOwner() != null ? savingsAccount.getSecondaryOwner().getName() : "No secondary owner assigned");
        }

        StudentAccount studentAccount = studentAccountRepository.findAccountById(user.getId(), id);
        if(studentAccount != null){
            return new AccountVM(studentAccount.getId(), studentAccount.getBalance(), studentAccount.getClass().getSimpleName(), studentAccount.getPrimaryOwner().getName(),
                    studentAccount.getSecondaryOwner() != null ? studentAccount.getSecondaryOwner().getName() : "No secondary owner assigned");
        }

        CreditCard creditCard = creditCardRepository.findAccountById(user.getId(), id);
        if(creditCard != null){
            creditCard.applyMonthlyInterest();
            creditCardRepository.save(creditCard);
            return new AccountVM(creditCard.getId(), creditCard.getBalance(), creditCard.getClass().getSimpleName(), creditCard.getPrimaryOwner().getName(),
                    creditCard.getSecondaryOwner() != null ? creditCard.getSecondaryOwner().getName() : "No secondary owner assigned");
        }

        throw new ResourceNotFoundException("Account with id " + id + " not found");
    }
}
