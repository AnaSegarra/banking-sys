package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.Role;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.RoleRepository;
import com.segarra.bankingsystem.models.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountHolderServiceTest {
    @Autowired
    private AccountHolderService accountHolderService;

    @MockBean
    private AccountHolderRepository accountHolderRepository;
    @MockBean
    private RoleRepository roleRepository;

    private List<AccountHolder> accountHolderList;


    @BeforeEach
    void setUp() {
        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s",
                new Address("Spain", "Sabadell", "Carrer de l'Estrella", 6, "08201"));
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28700"), "1234", "gema_s");

        accountHolderList = Arrays.asList(accountHolder, accountHolder2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Unit test - retrieval of all account holders")
    void getAll() {
        when(accountHolderRepository.findAll()).thenReturn(accountHolderList);
        assertEquals(2, accountHolderService.getAll().size());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Unit test - signup of new account holder")
    void create() {
        AccountHolder accountHolder = new AccountHolder("Sergio", LocalDate.of(2020, 1, 28),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28700"), "1234", "gabi_c");
        Role role = new Role("ROLE_ACCOUNTHOLDER", accountHolder);
        when(accountHolderRepository.save(any(AccountHolder.class))).thenReturn(accountHolder);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        AccountHolder savedAccHolder = accountHolderService.create(accountHolder);
        assertEquals("Sergio", savedAccHolder.getName());
    }
}