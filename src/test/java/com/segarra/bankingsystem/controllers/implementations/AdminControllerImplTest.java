package com.segarra.bankingsystem.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segarra.bankingsystem.dto.FinanceAdminRequest;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import com.segarra.bankingsystem.models.Address;
import com.segarra.bankingsystem.models.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerImplTest {

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private CheckingAccount checkingAccount;
    private SavingsAccount savingsAccount;
    private StudentAccount studentAccount;
    private CreditCard creditCard;
    private FinanceAdminRequest debitRequest;
    private FinanceAdminRequest creditRequest;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");

        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));

        checkingAccount = new CheckingAccount(accountHolder,
                new Money(new BigDecimal("2000")), 1234);
        checkingAccountRepository.save(checkingAccount);

        savingsAccount = new SavingsAccount(accountHolder,
                new Money(new BigDecimal("1000")), new BigDecimal("0.15"), 1234, new BigDecimal("200"));
        savingsAccountRepository.save(savingsAccount);

        studentAccount = new StudentAccount(accountHolder, new Money(new BigDecimal("3000")), 1234);
        studentAccountRepository.save(studentAccount);

        creditCard = new CreditCard(accountHolder, new Money(new BigDecimal("4000")), new BigDecimal("200"), new BigDecimal("0.12"));
        creditCardRepository.save(creditCard);

        // ====
        debitRequest = new FinanceAdminRequest(new BigDecimal("100"), "debit");
        creditRequest = new FinanceAdminRequest(new BigDecimal("200"), "credit");
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test debit checking account, expected reduced balance")
    void financeAccount_DebitCheckingAccount() throws Exception {
        mockMvc.perform(post("/api/v1/accounts/" + checkingAccount.getId())
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(debitRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals( new BigDecimal("1900.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
    }

    @Test
    @DisplayName("Test credit savings account, expected increased balance")
    void financeAccount_CreditSavingsAccount() throws Exception {
        mockMvc.perform(post("/api/v1/accounts/" + savingsAccount.getId())
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(creditRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals( new BigDecimal("1200.00"), savingsAccountRepository.findById(savingsAccount.getId()).get().getBalance().getAmount());
    }

    @Test
    @DisplayName("Test debit credit card, expected reduced balance")
    void financeAccount_DebitCreditCard() throws Exception {
        mockMvc.perform(post("/api/v1/accounts/" + creditCard.getId())
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(debitRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals( new BigDecimal("3900.00"), creditCardRepository.findById(creditCard.getId()).get().getBalance().getAmount());
    }

    @Test
    @DisplayName("Test debit student account, expected reduced balance")
    void financeAccount_DebitStudentAccount() throws Exception {
        mockMvc.perform(post("/api/v1/accounts/" + studentAccount.getId())
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(debitRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        assertEquals( new BigDecimal("2900.00"), studentAccountRepository.findById(studentAccount.getId()).get().getBalance().getAmount());
    }


    // TEST INVALID REQUESTS
    @Test
    @DisplayName("Test debit/credit an account that doesn't exist, expected expected 400 status code")
    void financeAccount_InvalidAccountId() throws Exception {
        mockMvc.perform(post("/api/v1/accounts/20")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(debitRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Test finance account with invalid operation type, expected expected 400 status code")
    void financeAccount_InvalidOperationType() throws Exception {
        FinanceAdminRequest invalidRequest = new FinanceAdminRequest(new BigDecimal("200"), "finance");

        mockMvc.perform(post("/api/v1/accounts/" + checkingAccount.getId())
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(invalidRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

    }
}