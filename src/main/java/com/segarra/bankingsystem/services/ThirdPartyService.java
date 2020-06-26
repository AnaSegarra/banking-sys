package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.exceptions.IllegalInputException;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.Role;
import com.segarra.bankingsystem.models.ThirdPartyUser;
import com.segarra.bankingsystem.models.User;
import com.segarra.bankingsystem.repositories.RoleRepository;
import com.segarra.bankingsystem.repositories.ThirdPartyRepository;
import com.segarra.bankingsystem.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private static final Logger LOGGER = LogManager.getLogger(AccountHolderService.class);

    @Secured({"ROLE_ADMIN"})
    public ThirdPartyUser create(ThirdPartyUser thirdPartyUser){
        User foundUser = userRepository.findByUsername(thirdPartyUser.getUsername());
        if(foundUser != null){
            LOGGER.info("Username taken - throw exception; status code 400");
            throw new IllegalInputException("This username has already been taken");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        thirdPartyUser.setPassword(passwordEncoder.encode(thirdPartyUser.getPassword()));
        ThirdPartyUser newUser = thirdPartyRepository.save(thirdPartyUser);
        Role role = new Role("ROLE_THIRDPARTY", newUser);
        roleRepository.save(role);
        LOGGER.info("Third party user created " + newUser);
        return newUser;
    }
}
