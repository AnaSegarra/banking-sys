package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CreditCard;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CreditCardRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CreditCardControllerImplTest {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private AccountHolder accountHolder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        accountHolder = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
        AccountHolder accountHolder2 = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));

        CreditCard creditCard = new CreditCard(accountHolder, new Money(new BigDecimal("4000")), new BigDecimal("200"), new BigDecimal("0.12"));
        CreditCard creditCard2 = new CreditCard(accountHolder2, new Money(new BigDecimal("6000")), new BigDecimal("300"), new BigDecimal("0.12"));
        creditCardRepository.saveAll(Stream.of(creditCard, creditCard2).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get request to retrieve all credit cards")
    void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/credit-cards")).andExpect(status().isOk());
    }
}