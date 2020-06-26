package com.segarra.bankingsystem.controllers.implementations;

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

    @Test
    @DisplayName("Test post request to create a checking account")
    void create_CheckingAccount() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "checking")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ",\"secretKey\": 1234, \"balance\": {\"amount\": 12000}}")
                .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).andReturn();
        // minimum balance field is only present in checking accounts
        assertTrue(result.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    @DisplayName("Test post request to create a student checking account when primary account holder is under 24")
    void create_StudentCheckingAccount() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "checking")
                .content("{\"primaryOwnerId\":" + youngAccHolder.getId() + ",\"secretKey\": 1234, \"balance\": {\"amount\": 12000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        // should create a student checking account which doesn't have a minimum balance
        assertFalse(result.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with default values for creditLimit and interestRate")
    void createCreditCard_DefaultValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "credit-card")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertFalse(result.getResponse().getContentAsString().contains("\"creditLimit\": 100"));
        assertFalse(result.getResponse().getContentAsString().contains("\"interestRate\": 0.2"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with chosen values for creditLimit and interestRate")
    void createCreditCard_ChosenValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "credit-card")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"creditCardLimit\": 200, \"cardInterestRate\": 0.15}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertFalse(result.getResponse().getContentAsString().contains("\"creditCardLimit\": 200"));
        assertFalse(result.getResponse().getContentAsString().contains("\"cardInterestRate\": 0.15"));
    }

    @Test
    @DisplayName("Test post request to create a credit card with value above limit for creditLimit, expected 400 status code")
    void createCreditCard_InvalidCreditLimit() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type","credit-card")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"creditCardLimit\": 10}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request to create a savings account with default values for minimumBalance and interestRate")
    void createSavingsAccount_DefaultValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "savings")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertFalse(result.getResponse().getContentAsString().contains("\"savingsInterestRate\": 0.0025"));
        assertFalse(result.getResponse().getContentAsString().contains("\"savingsMinimumBalance\": 1000"));
    }

    @Test
    @DisplayName("Test post request to create a savings account with chosen values for minimumBalance and interestRate")
    void createSavingsAccount_ChosenValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "savings")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"savingsInterestRate\": 0.4, \"savingsMinimumBalance\": 800}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertFalse(result.getResponse().getContentAsString().contains("\"savingsInterestRate\": 0.4"));
        assertFalse(result.getResponse().getContentAsString().contains("\"savingsMinimumBalance\": 800"));
    }

    @Test
    @DisplayName("Test post request to create a savings account with value above limit for interestRate, expected 400 status code")
    void createSavingsAccount_InvalidInterestRate() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type","savings")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"balance\": {\"amount\": 1000}, \"savingsInterestRate\": 10}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    // test invalid requests (wrong owner id || wrong param)
    @Test
    @DisplayName("Test post request with wrong primary owner id, expected 400 status code")
    void create_invalidPrimaryOwnerId() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type","checking")
                .content("{\"primaryOwnerId\": 20, \"secretKey\": 1234, \"balance\": {\"amount\": 12000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request with wrong secondary owner id, expected 400 status code")
    void create_invalidSecondaryOwnerId() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type","checking")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ",\"secondaryOwnerId\": 20, \"secretKey\": 1234, \"balance\": {\"amount\": 12000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test post request with wrong account type, expected 400 status code")
    void create_invalidAccountType() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                .with(user("admin").roles("ADMIN")).param("type", "student-account")
                .content("{\"primaryOwnerId\":" + accountHolder.getId() + ", \"secretKey\": 1234, \"balance\": {\"amount\": 12000}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}