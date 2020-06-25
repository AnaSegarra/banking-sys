package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountVM;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<AccountHolder> getAll(){
        return accountHolderRepository.findAll();
    }

    public AccountHolder create(AccountHolder accountHolder){
        Optional<AccountHolder> foundUser = accountHolderRepository.findByUsername(accountHolder.getUsername());
        if(foundUser.isPresent()){
            LOGGER.info("Username taken - throw exception; status code 400");
            throw new IllegalInputException("This username has already been taken");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword()));
        AccountHolder newUser = accountHolderRepository.save(accountHolder);
        Role role = new Role("ROLE_ACCOUNTHOLDER", newUser);
        roleRepository.save(role);
        LOGGER.info("Accountholder created " + newUser);
        return newUser;
    }

    @PreAuthorize("authenticated")
    public List<AccountVM> getAllAccounts(User user){
        List<Account> accounts = accountRepository.findAllUserAccounts(user.getId());
        return accounts.stream().map(account -> new AccountVM(account.getId(), account.getBalance(),
                account.getClass().getSimpleName())).collect(Collectors.toList());
    }
}
