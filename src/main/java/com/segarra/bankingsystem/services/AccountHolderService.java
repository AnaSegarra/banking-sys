package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountHolderRequest;
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
                        accountHolder.getBirthday(), accountHolder.getPrimaryAddress(),
                        accountHolder.getMailingAddress())).collect(Collectors.toList());
    }

    // create new account holder - admin restricted
    @Secured({"ROLE_ADMIN"})
    public AccountHolderVM create(AccountHolderRequest newUser){
        User foundUser = userRepository.findByUsername(newUser.getUsername());
        if(foundUser != null){
            LOGGER.error("Controlled exception - Username " + newUser.getUsername() + " is already taken");
            throw new IllegalInputException("Username " + newUser.getUsername() + " is already taken");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        AccountHolder accountHolder = new AccountHolder(newUser.getUsername(), newUser.getBirthday(), newUser.getPrimaryAddress(), newUser.getMailingAddress());
        accountHolderRepository.save(accountHolder);
        ClientUser clientUser = new ClientUser(newUser.getUsername(), passwordEncoder.encode(newUser.getPassword()), accountHolder);
        userRepository.save(clientUser);
        Role role = new Role("ROLE_ACCOUNTHOLDER", clientUser);
        roleRepository.save(role);
        LOGGER.info("Account holder created " + newUser);

        return new AccountHolderVM(accountHolder.getId(), accountHolder.getName(), accountHolder.getBirthday(),
                accountHolder.getPrimaryAddress(), accountHolder.getMailingAddress());
    }
}
