package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHolderService {
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<AccountHolder> getAll(){
        return accountHolderRepository.findAll();
    }

    public AccountHolder create(AccountHolder accountHolder){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User(accountHolder.getName(), passwordEncoder.encode(accountHolder.getPassword()));
        userRepository.save(user);
        Role role = new Role("ROLE_ACCOUNTHOLDER", user);
        roleRepository.save(role);
        accountHolder.setPassword(user.getPassword());
        return accountHolderRepository.save(accountHolder);
    }
}
