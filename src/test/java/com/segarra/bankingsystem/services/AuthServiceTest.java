package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.RoleRepository;
import com.segarra.bankingsystem.repositories.UserRepository;
import com.segarra.bankingsystem.utils.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @MockBean
    private AccountHolderRepository accountHolderRepository;

    @Test
    @DisplayName("Unit test - signup of new account holder")
    void create() {
        AccountHolder accountHolder = new AccountHolder("Sergio", LocalDate.of(2020, 1, 28),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28700"), "1234");
        when(accountHolderRepository.save(any(AccountHolder.class))).thenReturn(accountHolder);

        AccountHolder savedAccHolder = authService.create(accountHolder);
        assertEquals("Sergio", savedAccHolder.getName());
    }
}