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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"));
        AccountHolder accountHolder2 = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"));
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
        mockMvc.perform(get("/credit-cards")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test post request to create a credit card with default values for creditLimit and interestRate")
    void createCreditCard_DefaultValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/credit-cards")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("\"creditLimit\": 100"));
        assertFalse(result.getResponse().getContentAsString().contains("\"interestRate\": 0.2"));

    }

    @Test
    @DisplayName("Test post request to create a credit card with chosen values for creditLimit and interestRate")
    void createCreditCard_ChosenValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/credit-cards")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"creditLimit\": 200, \"interestRate\": 0.15}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertFalse(result.getResponse().getContentAsString().contains("\"creditLimit\": 200"));
        assertFalse(result.getResponse().getContentAsString().contains("\"interestRate\": 0.15"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with value above limit for creditLimit, expected 400 status code")
    void createCreditCard_InvalidCreditLimit() throws Exception {
        mockMvc.perform(post("/credit-cards")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"creditLimit\": 10}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request to create a credit card with value below limit for interestRate, expected 400 status code")
    void createCreditCard_InvalidInterestRate() throws Exception {
        mockMvc.perform(post("/credit-cards")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"interestRate\": 0.9}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request with wrong secondary owner id, expected 400 status code")
    void create_invalidSecondaryOwnerId() throws Exception {
        mockMvc.perform(post("/credit-cards")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ",\"secondaryOwnerId\": 20, \"balance\": {\"amount\": 12000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}