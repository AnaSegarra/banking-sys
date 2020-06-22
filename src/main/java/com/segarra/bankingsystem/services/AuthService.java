package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.Role;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.RoleRepository;
import com.segarra.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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
