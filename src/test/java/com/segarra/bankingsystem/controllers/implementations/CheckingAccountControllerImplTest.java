package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CheckingAccountRepository;
import com.segarra.bankingsystem.repositories.StudentAccountRepository;
import com.segarra.bankingsystem.utils.Address;
import com.segarra.bankingsystem.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CheckingAccountControllerImplTest {
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private AccountHolder accountHolder;
    private AccountHolder youngAccHolder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"));
        accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"));
        youngAccHolder = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
                new Address("Spain", "Madrid", "Luna Avenue", 8, "28200"));
        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2, youngAccHolder).collect(Collectors.toList()));

        CheckingAccount checkingAccount = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("2000")), 1234);
        CheckingAccount checkingAccount2 = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("5000")), 1234);

        checkingAccountRepository.saveAll(Stream.of(checkingAccount, checkingAccount2).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get request to retrieve all checking accounts")
    void getAll() throws Exception {
        mockMvc.perform(get("/checking-accounts")).andExpect(status().isOk());
    }
}