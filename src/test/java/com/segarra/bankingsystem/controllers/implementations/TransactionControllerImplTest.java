package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.enums.Status;
import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import com.segarra.bankingsystem.utils.Address;
import com.segarra.bankingsystem.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerImplTest {
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
    private RoleRepository roleRepository;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private CheckingAccount checkingAccount;
    private CheckingAccount checkingAccount2;
    private SavingsAccount savingsAccount;
    private SavingsAccount savingsAccount2;
    private StudentAccount studentAccount;
    private StudentAccount studentAccount2;
    private CreditCard creditCard;
    private CreditCard creditCard2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");

        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword())); // encode user's password
        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));

        Role role = new Role("ROLE_ACCOUNTHOLDER", accountHolder);
        roleRepository.save(role);

        checkingAccount = new CheckingAccount(accountHolder,
                new Money(new BigDecimal("2000")), 1234);
        checkingAccount2 = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("5000")), 1234);

        checkingAccountRepository.saveAll(Stream.of(checkingAccount, checkingAccount2).collect(Collectors.toList()));

        savingsAccount = new SavingsAccount(accountHolder,
                new Money(new BigDecimal("1000")), new BigDecimal("0.15"), 1234, new BigDecimal("200"));
        savingsAccount2 = new SavingsAccount(accountHolder2,
                new Money(new BigDecimal("3000")), new BigDecimal("0.15"), 1234, new BigDecimal("200"));

        savingsAccountRepository.saveAll(Stream.of(savingsAccount, savingsAccount2).collect(Collectors.toList()));

        studentAccount = new StudentAccount(accountHolder, new Money(new BigDecimal("3000")), 1234);
        studentAccount2 = new StudentAccount(accountHolder2, new Money(new BigDecimal("1500")), 1234);
        studentAccountRepository.saveAll(Stream.of(studentAccount, studentAccount2).collect(Collectors.toList()));

        creditCard = new CreditCard(accountHolder, new Money(new BigDecimal("4000")), new BigDecimal("200"), new BigDecimal("0.12"));
        creditCard2 = new CreditCard(accountHolder2, new Money(new BigDecimal("6000")), new BigDecimal("300"), new BigDecimal("0.12"));
        creditCardRepository.saveAll(Stream.of(creditCard, creditCard2).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
    }

    // TEST SUCCESSFUL TRANSACTIONS
    @Test
    @DisplayName("Test successful transaction between checking accounts, expected reduced balance and 204 status code")
    void makeTransaction_CheckingAccounts() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "checking")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + checkingAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals( new BigDecimal("1800.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
    }

    @Test
    @DisplayName("Test successful transaction between savings accounts, expected reduced balance and 204 status code")
    void makeTransaction_SavingsAccounts() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","savings")
                .param("sender", "savings")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ savingsAccount2.getId() + ", \"senderId\":" + savingsAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals( new BigDecimal("800.00"), savingsAccountRepository.findById(savingsAccount.getId()).get().getBalance().getAmount());
    }

    @Test
    @DisplayName("Test successful transaction between student accounts, expected reduced balance and 204 status code")
    void makeTransaction_StudentAccounts() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","student")
                .param("sender", "student")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ studentAccount2.getId() + ", \"senderId\":" + studentAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals( new BigDecimal("2800.00"), studentAccountRepository.findById(studentAccount.getId()).get().getBalance().getAmount());
    }

    @Test
    @DisplayName("Test successful transaction between credit cards, expected reduced balance and 204 status code")
    void makeTransaction_CreditCards() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","credit-card")
                .param("sender", "credit-card")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ creditCard2.getId() + ", \"senderId\":" + creditCard.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals( new BigDecimal("3800.00"), creditCardRepository.findById(creditCard.getId()).get().getBalance().getAmount());
    }


    // TEST UNSUCCESSFUL TRANSACTIONS
    @Test
    @DisplayName("Test transaction between accounts with the same id, expected 400 status code ")
    void makeTransaction_SameAccount() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "checking")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount.getId() + ", \"senderId\":" + checkingAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction from checking account not owned by logged user, expected 403 status code")
    void makeTransaction_CheckingAccount_Forbidden() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","savings")
                .param("sender", "checking")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ savingsAccount2.getId() + ", \"senderId\":" + checkingAccount2.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test transaction from savings account not owned by logged user, expected 403 status code")
    void makeTransaction_SavingsAccount_Forbidden() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "savings")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + savingsAccount2.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test transaction from student account not owned by logged user, expected 403 status code")
    void makeTransaction_StudentAccount_Forbidden() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "student")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + studentAccount2.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test transaction from credit card not owned by logged user, expected 403 status code")
    void makeTransaction_CreditCard_Forbidden() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "credit-card")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + creditCard2.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test transaction with checking account frozen, expected 400 status code")
    void makeTransaction_CheckingAccountFrozen_BadRequest() throws Exception {
        checkingAccount.setStatus(Status.FROZEN);
        checkingAccountRepository.save(checkingAccount);
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "checking")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + checkingAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction with savings account frozen, expected 400 status code")
    void makeTransaction_SavingsAccountFrozen_BadRequest() throws Exception {
        savingsAccount.setStatus(Status.FROZEN);
        savingsAccountRepository.save(savingsAccount);
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "savings")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + savingsAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction with student account frozen, expected 400 status code")
    void makeTransaction_StudentAccountFrozen_BadRequest() throws Exception {
        studentAccount.setStatus(Status.FROZEN);
        studentAccountRepository.save(studentAccount);
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "student")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + studentAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction from checking account with not enough funds, expected 400 status code")
    void makeTransaction_CheckingAccountNoFunds_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "checking")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + checkingAccount.getId() + ", \"amount\": 2500}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction from savings account with not enough funds, expected 400 status code")
    void makeTransaction_SavingsAccountNoFunds_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "savings")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + savingsAccount.getId() + ", \"amount\": 1500}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction from student account with not enough funds, expected 400 status code")
    void makeTransaction_StudentAccountNoFunds_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "student")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + studentAccount.getId() + ", \"amount\": 3100}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test transaction from credit card with not enough funds, expected 400 status code")
    void makeTransaction_CreditCardNoFunds_BadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "credit-card")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + creditCard.getId() + ", \"amount\": 4100}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}