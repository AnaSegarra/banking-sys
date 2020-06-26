package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.SavingsAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.SavingsAccountRepository;
import com.segarra.bankingsystem.models.Address;
import com.segarra.bankingsystem.models.Money;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SavingsAccountControllerImplTest {
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));

        SavingsAccount savingsAccount = new SavingsAccount(accountHolder2,
                new Money(new BigDecimal("2000")), new BigDecimal("0.15"), 1234, new BigDecimal("200"));
        SavingsAccount savingsAccount2 = new SavingsAccount(accountHolder,
                new Money(new BigDecimal("2000")), new BigDecimal("0.5"), 1234, new BigDecimal("800"));

        savingsAccountRepository.saveAll(Stream.of(savingsAccount, savingsAccount2).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
        savingsAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get request to retrieve all savings accounts")
    void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/savings-accounts")).andExpect(status().isOk());
    }
}