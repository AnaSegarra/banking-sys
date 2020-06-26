package com.segarra.bankingsystem.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segarra.bankingsystem.dto.AccountRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AccountControllerImplTest {
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private AccountHolder accountHolder;
    private AccountHolder youngAccHolder;
    private CheckingAccount checkingAccount;
    private SavingsAccount savingsAccount;
    private StudentAccount studentAccount;
    private CreditCard creditCard;
    private ObjectMapper objectMapper = new ObjectMapper();
    private AccountRequest accountRequest;
    private FinanceAdminRequest debitRequest;
    private FinanceAdminRequest creditRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
        accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
        youngAccHolder = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
                new Address("Spain", "Madrid", "Luna Avenue", 8, "28200"), "1234", "gabi_c");
        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2, youngAccHolder).collect(Collectors.toList()));

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

        accountRequest = new AccountRequest();
        accountRequest.setPrimaryOwnerId(accountHolder.getId());
        accountRequest.setSecretKey(1234);
        accountRequest.setBalance(new Money(new BigDecimal("12000")));
        accountRequest.setAccountType("checking");


        // ==== request objects
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
    @DisplayName("Test get by id of a checking account")
    void getById_CheckingAccount() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/" + checkingAccount.getId())
                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test get by id of a student account")
    void getById_StudentAccount() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/" + studentAccount.getId())
                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test get by id of a credit card")
    void getById_CreditCard() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/" + creditCard.getId())
                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test get by id of a savings account")
    void getById_SavingsAccount() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/" + savingsAccount.getId())
                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test get by id with an invalid account id, expected 400 status code")
    void getById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/" + 20)
                .with(user("admin").roles("ADMIN"))).andExpect(status().isBadRequest());
    }

    // TEST POST REQUESTS - create accounts
    @Test
    @DisplayName("Test post request to create a checking account")
    void create_CheckingAccount() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).andReturn();
        // minimum balance field is only present in checking accounts
        assertTrue(result.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    @DisplayName("Test post request to create a student checking account when primary account holder is under 24")
    void create_StudentCheckingAccount() throws Exception {
        accountRequest.setPrimaryOwnerId(youngAccHolder.getId());
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        // should create a student checking account which doesn't have a minimum balance
        assertFalse(result.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with default values for creditLimit and interestRate")
    void createCreditCard_DefaultValues() throws Exception {
        accountRequest.setAccountType("credit-card");
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"creditLimit\":100"));
        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.2"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with chosen values for creditLimit and interestRate")
    void createCreditCard_ChosenValues() throws Exception {
        accountRequest.setAccountType("credit-card");
        accountRequest.setCreditCardLimit(new BigDecimal("200"));
        accountRequest.setCardInterestRate(new BigDecimal("0.15"));

        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"creditLimit\":200"));
        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.15"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with value above limit for creditLimit, expected 400 status code")
    void createCreditCard_InvalidCreditLimit() throws Exception {
        accountRequest.setAccountType("credit-card");
        accountRequest.setCreditCardLimit(new BigDecimal("10"));
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request to create a savings account with default values for minimumBalance and interestRate")
    void createSavingsAccount_DefaultValues() throws Exception {
        accountRequest.setAccountType("savings");

        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.0025"));
        assertTrue(result.getResponse().getContentAsString().contains("\"minimumBalance\":1000"));
    }

    @Test
    @DisplayName("Test post request to create a savings account with chosen values for minimumBalance and interestRate")
    void createSavingsAccount_ChosenValues() throws Exception {
        accountRequest.setAccountType("savings");
        accountRequest.setSavingsInterestRate(new BigDecimal("0.4"));
        accountRequest.setSavingsMinimumBalance(new BigDecimal("800"));

        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.4"));
        assertTrue(result.getResponse().getContentAsString().contains("\"minimumBalance\":800"));
    }

    @Test
    @DisplayName("Test post request to create a savings account with value above limit for interestRate, expected 400 status code")
    void createSavingsAccount_InvalidInterestRate() throws Exception {
        accountRequest.setAccountType("savings");
        accountRequest.setSavingsInterestRate(new BigDecimal("10"));

        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    // test invalid requests (wrong owner id || wrong account type)
    @Test
    @DisplayName("Test post request with wrong primary owner id, expected 400 status code")
    void create_invalidPrimaryOwnerId() throws Exception {
        accountRequest.setPrimaryOwnerId(32);
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request with wrong secondary owner id, expected 400 status code")
    void create_invalidSecondaryOwnerId() throws Exception {
        accountRequest.setSecondaryOwnerId(20);
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request with wrong account type, expected 400 status code")
    void create_invalidAccountType() throws Exception {
        accountRequest.setAccountType("student");
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(accountRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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