package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountHolderService {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private static final Logger LOGGER = LogManager.getLogger(AccountHolderService.class);

    // retrieve every account holder - admin restricted
    @Secured({"ROLE_ADMIN"})
    public List<AccountHolderVM> getAllAccountHolders(){
        LOGGER.info("Admin GET request to retrieve every account holder");
        return accountHolderRepository.findAll().stream()
                .map(accountHolder -> new AccountHolderVM(accountHolder.getId(), accountHolder.getName(),
                        accountHolder.getUsername(), accountHolder.getBirthday(), accountHolder.getPrimaryAddress(),
                        accountHolder.getMailingAddress())).collect(Collectors.toList());
    }

    // create new account holder - admin restricted
    @Secured({"ROLE_ADMIN"})
    public AccountHolder create(AccountHolder accountHolder){
        User foundUser = userRepository.findByUsername(accountHolder.getUsername());
        if(foundUser != null){
            LOGGER.error("Controlled exception - Username " + accountHolder.getUsername() + " is already taken");
            throw new IllegalInputException("Username " + accountHolder.getUsername() + " is already taken");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword()));
        AccountHolder newUser = accountHolderRepository.save(accountHolder);
        Role role = new Role("ROLE_ACCOUNTHOLDER", newUser);
        roleRepository.save(role);
        LOGGER.info("Account holder created " + newUser);
        return newUser;
    }
}