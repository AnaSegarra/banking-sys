package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderService {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<AccountHolder> getAll(){
        return accountHolderRepository.findAll();
    }

    public AccountHolder create(AccountHolder accountHolder){
        Optional<AccountHolder> foundUser = accountHolderRepository.findByUsername(accountHolder.getUsername());
        if(foundUser.isPresent()){
            throw new IllegalInputException("This username has already been taken");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User(accountHolder.getUsername(), passwordEncoder.encode(accountHolder.getPassword()));
        userRepository.save(user);
        Role role = new Role("ROLE_ACCOUNTHOLDER", user);
        roleRepository.save(role);
        accountHolder.setPassword(user.getPassword());
        return accountHolderRepository.save(accountHolder);
    }
}
