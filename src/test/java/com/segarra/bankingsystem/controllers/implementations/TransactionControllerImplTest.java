package com.segarra.bankingsystem.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segarra.bankingsystem.dto.TransactionRequest;
import com.segarra.bankingsystem.enums.Status;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerImplTest {
//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private SavingsAccountRepository savingsAccountRepository;
//    @Autowired
//    private CheckingAccountRepository checkingAccountRepository;
//    @Autowired
//    private CreditCardRepository creditCardRepository;
//    @Autowired
//    private StudentAccountRepository studentAccountRepository;
//    @Autowired
//    private AccountHolderRepository accountHolderRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private WebApplicationContext wac;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private MockMvc mockMvc;
//    private AccountHolder accountHolder;
//    private CheckingAccount checkingAccount;
//    private CheckingAccount checkingAccount2;
//    private SavingsAccount savingsAccount;
//    private SavingsAccount savingsAccount2;
//    private StudentAccount studentAccount;
//    private StudentAccount studentAccount2;
//    private CreditCard creditCard;
//    private CreditCard creditCard2;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
//
//        accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
//                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
//        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
//                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
//
//        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword())); // encode user's password
//        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));
//
//        Role role = new Role("ROLE_ACCOUNTHOLDER", accountHolder);
//        roleRepository.save(role);
//
//        checkingAccount = new CheckingAccount(accountHolder,
//                new Money(new BigDecimal("2000")), "1234");
//        checkingAccount2 = new CheckingAccount(accountHolder2,
//                new Money(new BigDecimal("5000")), "1234");
//
//        checkingAccountRepository.saveAll(Stream.of(checkingAccount, checkingAccount2).collect(Collectors.toList()));
//
//        savingsAccount = new SavingsAccount(accountHolder,
//                new Money(new BigDecimal("1000")), new BigDecimal("0.15"), "1234", new BigDecimal("200"));
//        savingsAccount2 = new SavingsAccount(accountHolder2,
//                new Money(new BigDecimal("3000")), new BigDecimal("0.15"), "1234", new BigDecimal("200"));
//
//        savingsAccountRepository.saveAll(Stream.of(savingsAccount, savingsAccount2).collect(Collectors.toList()));
//
//        studentAccount = new StudentAccount(accountHolder, new Money(new BigDecimal("3000")), "1234");
//        studentAccount2 = new StudentAccount(accountHolder2, new Money(new BigDecimal("1500")), "1234");
//        studentAccountRepository.saveAll(Stream.of(studentAccount, studentAccount2).collect(Collectors.toList()));
//
//        creditCard = new CreditCard(accountHolder, new Money(new BigDecimal("4000")), new BigDecimal("200"), new BigDecimal("0.12"));
//        creditCard2 = new CreditCard(accountHolder2, new Money(new BigDecimal("6000")), new BigDecimal("300"), new BigDecimal("0.12"));
//        creditCardRepository.saveAll(Stream.of(creditCard, creditCard2).collect(Collectors.toList()));
//    }
//
//    @AfterEach
//    void tearDown() {
//        transactionRepository.deleteAll();
//        checkingAccountRepository.deleteAll();
//        studentAccountRepository.deleteAll();
//        savingsAccountRepository.deleteAll();
//        creditCardRepository.deleteAll();
//        accountHolderRepository.deleteAll();
//        roleRepository.deleteAll();
//    }
//
//    // TEST SUCCESSFUL TRANSACTIONS
//    @Test
//    @DisplayName("Test successful transaction between checking accounts, expected 204 status code")
//    void makeTransaction_CheckingAccounts() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), checkingAccount.getId(), new BigDecimal("200"));
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // sender account balance reduced by 200
//        assertEquals( new BigDecimal("1800.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
//        // recipient account balance increased by 200
//        assertEquals( new BigDecimal("5200.00"), checkingAccountRepository.findById(checkingAccount2.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test successful transaction between savings accounts, expected 204 status code")
//    void makeTransaction_SavingsAccounts() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", savingsAccount2.getId(), savingsAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // sender account balance reduced by 200
//        assertEquals( new BigDecimal("800.00"), savingsAccountRepository.findById(savingsAccount.getId()).get().getBalance().getAmount());
//        // recipient account balance increased by 200
//        assertEquals( new BigDecimal("3200.00"), savingsAccountRepository.findById(savingsAccount2.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test successful transaction between student accounts, expected reduced balance and 204 status code")
//    void makeTransaction_StudentAccounts() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", studentAccount2.getId(), studentAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // sender account balance reduced by 200
//        assertEquals( new BigDecimal("2800.00"), studentAccountRepository.findById(studentAccount.getId()).get().getBalance().getAmount());
//        // recipient account balance increased by 200
//        assertEquals( new BigDecimal("1700.00"), studentAccountRepository.findById(studentAccount2.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test successful transaction between credit cards, expected reduced balance and 204 status code")
//    void makeTransaction_CreditCards() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", creditCard2.getId(), creditCard.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        // sender account balance reduced by 200
//        assertEquals( new BigDecimal("3800.00"), creditCardRepository.findById(creditCard.getId()).get().getBalance().getAmount());
//        // recipient account balance increased by 200
//        assertEquals( new BigDecimal("6200.00"), creditCardRepository.findById(creditCard2.getId()).get().getBalance().getAmount());
//    }
//
//
//    // TEST UNSUCCESSFUL TRANSACTIONS
//    @Test
//    @DisplayName("Test transaction between accounts with the same id, expected 400 status code ")
//    void makeTransaction_SameAccount() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount.getId(), checkingAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction from checking account not owned by logged user, expected 403 status code")
//    void makeTransaction_CheckingAccount_Forbidden() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", savingsAccount2.getId(), checkingAccount2.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("Test transaction from savings account not owned by recipient's name, expected 400 status code")
//    void makeTransaction_SavingsAccount_Forbidden() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gabi", checkingAccount2.getId(), savingsAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction with checking account frozen, expected 400 status code")
//    void makeTransaction_CheckingAccountFrozen_BadRequest() throws Exception {
//        checkingAccount.setStatus(Status.FROZEN);
//        checkingAccountRepository.save(checkingAccount);
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), checkingAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction with savings account frozen, expected 400 status code")
//    void makeTransaction_SavingsAccountFrozen_BadRequest() throws Exception {
//        savingsAccount.setStatus(Status.FROZEN);
//        savingsAccountRepository.save(savingsAccount);
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), savingsAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction with student account frozen, expected 400 status code")
//    void makeTransaction_StudentAccountFrozen_BadRequest() throws Exception {
//        studentAccount.setStatus(Status.FROZEN);
//        studentAccountRepository.save(studentAccount);
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), studentAccount.getId(), new BigDecimal("200"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction from checking account with not enough funds, expected 400 status code")
//    void makeTransaction_CheckingAccountNoFunds_BadRequest() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), checkingAccount.getId(), new BigDecimal("2500"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction from savings account with not enough funds, expected 400 status code")
//    void makeTransaction_SavingsAccountNoFunds_BadRequest() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), savingsAccount.getId(), new BigDecimal("1500"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction from student account with not enough funds, expected 400 status code")
//    void makeTransaction_StudentAccountNoFunds_BadRequest() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), studentAccount.getId(), new BigDecimal("3100"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test transaction from credit card with not enough funds, expected 400 status code")
//    void makeTransaction_CreditCardNoFunds_BadRequest() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), creditCard.getId(), new BigDecimal("4100"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    // TEST FRAUD DETECTION
//    @Test
//    @DisplayName("Test fraud detection - two transactions from a savings account within a 1 second period, expected 400 status code")
//    void makeTransaction_FraudDetectionSavings_ConsecutiveTransactions() throws Exception {
//        Transaction transaction = new Transaction(savingsAccount, checkingAccount2, new BigDecimal("10"));
//        transactionRepository.save(transaction);
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + savingsAccount.getId() + ", \"amount\": 100}")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test fraud detection - daily transactions from a checking account that total to more thant 150% of the highest amount any other day, expected 400 status code")
//    void makeTransaction_FraudDetectionChecking_HighestAmount() throws Exception {
//        Transaction previousDay = new Transaction(checkingAccount, checkingAccount2, new BigDecimal("200"));
//        Transaction firstTransaction = new Transaction(checkingAccount, checkingAccount2, new BigDecimal("100"));
//        previousDay.setDate(firstTransaction.getDate().minusMonths(1L));
//        transactionRepository.saveAll(Stream.of(previousDay, firstTransaction).collect(Collectors.toList()));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + checkingAccount.getId() + ", \"amount\": 600}")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test fraud detection - two transactions from a student account within a 1 second period, expected 400 status code")
//    void makeTransaction_FraudDetectionStudent_ConsecutiveTransactions() throws Exception {
//        Transaction transaction = new Transaction(studentAccount, checkingAccount2, new BigDecimal("10"));
//        transactionRepository.save(transaction);
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + studentAccount.getId() + ", \"amount\": 100}")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//
//    // Test transactions with fees applied
//    @Test
//    @DisplayName("Test transaction from savings account that drops below minimum balance so penalty fee is applied")
//    void makeTransaction_ApplyPenaltyFee() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), savingsAccount.getId(), new BigDecimal("900"));
//
//        MvcResult result = mockMvc.perform(post("/api/v1/transactions")
//                    .with(httpBasic("ana_s", "1234"))
//                    .content(objectMapper.writeValueAsString(transaction))
//                    .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNoContent()).andReturn();
//
//        assertEquals( new BigDecimal("60.00"), savingsAccountRepository.findById(savingsAccount.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test monthly maintenance fee being applied to checking account during transaction")
//    void makeTransaction_ApplyMonthlyMaintenanceFee() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), checkingAccount.getId(), new BigDecimal("100"));
//        checkingAccount.setLastFeeApplied(checkingAccount.getLastFeeApplied().minusMonths(2));
//        checkingAccountRepository.save(checkingAccount);
//
//        MvcResult result = mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent()).andReturn();
//
//        /* monthly maintenance fee of 12 applied two times (24) and minus 100 from transaction
//           with a starting balance of 2000 results in 1876
//         */
//        assertEquals( new BigDecimal("1876.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test monthly interest rate being applied to credit card during transaction")
//    void makeTransaction_ApplyMonthlyInterest() throws Exception {
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), creditCard.getId(), new BigDecimal("100"));
//        creditCard.setLastInterestApplied(creditCard.getLastInterestApplied().minusMonths(2));
//        creditCardRepository.save(creditCard);
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        /* monthly interest rate of 0.12 (0.01 per month) applied two times increases a starting
//            balance of 4000 to 4080.4 and minus 100 from transaction results in 3980.4
//        */
//        assertEquals( new BigDecimal("3980.40"), creditCardRepository.findById(creditCard.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test annual interest rate being applied to savings account during transaction")
//    void makeTransaction_ApplyAnnualInterest() throws Exception {
//        SavingsAccount newSavings = new SavingsAccount(accountHolder, new Money(new BigDecimal("1000000")), new BigDecimal("0.01"), "1234", new BigDecimal("200"));
//        newSavings.setLastInterestApplied(newSavings.getLastInterestApplied().minusYears(1));
//        savingsAccountRepository.save(newSavings);
//        TransactionRequest transaction = new TransactionRequest("Gema", checkingAccount2.getId(), newSavings.getId(), new BigDecimal("1500"));
//
//        mockMvc.perform(post("/api/v1/transactions")
//                .with(httpBasic("ana_s", "1234"))
//                .content(objectMapper.writeValueAsString(transaction))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        /* annual interest rate of 0.01 applied increases a starting balance of 1M to 1.010.000
//            and minus 1500 from transaction results in 1.008.500
//        */
//        assertEquals( new BigDecimal("1008500.00"), savingsAccountRepository.findById(newSavings.getId()).get().getBalance().getAmount());
//    }
}