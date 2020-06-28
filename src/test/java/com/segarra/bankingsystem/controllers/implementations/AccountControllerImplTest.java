package com.segarra.bankingsystem.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segarra.bankingsystem.dto.AccountRequest;
import com.segarra.bankingsystem.dto.FinanceAdminRequest;
import com.segarra.bankingsystem.dto.FinanceThirdPartyRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
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
    private FinanceThirdPartyRequest thirdPartyDebitRequest;
    private FinanceThirdPartyRequest thirdPartyCreditRequest;
    private FinanceThirdPartyRequest thirdPartyInvalidRequest;


//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
//
//        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
//                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
//        accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
//                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
//        youngAccHolder = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
//                new Address("Spain", "Madrid", "Luna Avenue", 8, "28200"), "1234", "gabi_c");
//
//        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword())); // encode user's password
//        accountHolderRepository.save(accountHolder);
//        Role role = new Role("ROLE_ACCOUNTHOLDER", accountHolder);
//        roleRepository.save(role);
//
//        accountHolderRepository.saveAll(Stream.of( accountHolder2, youngAccHolder).collect(Collectors.toList()));
//
//        checkingAccount = new CheckingAccount(accountHolder,
//                new Money(new BigDecimal("2000")), "1234");
//        checkingAccountRepository.save(checkingAccount);
//
//        savingsAccount = new SavingsAccount(accountHolder,
//                new Money(new BigDecimal("1000")), new BigDecimal("0.15"), "1234", new BigDecimal("200"));
//        savingsAccountRepository.save(savingsAccount);
//
//        studentAccount = new StudentAccount(accountHolder, new Money(new BigDecimal("3000")), "1234");
//        studentAccountRepository.save(studentAccount);
//
//        creditCard = new CreditCard(accountHolder, new Money(new BigDecimal("4000")), new BigDecimal("200"), new BigDecimal("0.12"));
//        creditCardRepository.save(creditCard);
//
//        accountRequest = new AccountRequest();
//        accountRequest.setPrimaryOwnerId(accountHolder.getId());
//        accountRequest.setSecretKey("1234");
//        accountRequest.setBalance(new Money(new BigDecimal("12000")));
//        accountRequest.setAccountType("checking");
//
//
//        // ==== request objects
//        debitRequest = new FinanceAdminRequest(new BigDecimal("100"), "debit");
//        creditRequest = new FinanceAdminRequest(new BigDecimal("200"), "credit");
//        thirdPartyDebitRequest = new FinanceThirdPartyRequest(new BigDecimal("100"), "debit", "1234");
//        thirdPartyCreditRequest = new FinanceThirdPartyRequest(new BigDecimal("200"), "credit", "1234");
//        thirdPartyInvalidRequest = new FinanceThirdPartyRequest(new BigDecimal("200"), "credit", "3214");
//    }
//
//    @AfterEach
//    void tearDown() {
//        checkingAccountRepository.deleteAll();
//        studentAccountRepository.deleteAll();
//        savingsAccountRepository.deleteAll();
//        creditCardRepository.deleteAll();
//        accountHolderRepository.deleteAll();
//    }
//
//
//    /* ADMIN - get account by id */
//    @Test
//    @DisplayName("Test get by id of a checking account")
//    void getById_CheckingAccount() throws Exception {
//        mockMvc.perform(get("/api/v1/accounts/" + checkingAccount.getId())
//                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Test get by id of a student account")
//    void getById_StudentAccount() throws Exception {
//        mockMvc.perform(get("/api/v1/accounts/" + studentAccount.getId())
//                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Test get by id of a credit card")
//    void getById_CreditCard() throws Exception {
//        mockMvc.perform(get("/api/v1/accounts/" + creditCard.getId())
//                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Test get by id of a savings account")
//    void getById_SavingsAccount() throws Exception {
//        mockMvc.perform(get("/api/v1/accounts/" + savingsAccount.getId())
//                .with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Test get by id with an invalid account id, expected 400 status code")
//    void getById_InvalidId() throws Exception {
//        mockMvc.perform(get("/api/v1/accounts/" + 20)
//                .with(user("admin").roles("ADMIN"))).andExpect(status().isBadRequest());
//    }
//
//    /* TEST POST REQUESTS - create accounts */
//    @Test
//    @DisplayName("Test post request to create a checking account")
//    void create_CheckingAccount() throws Exception {
//        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON)).
//                andExpect(status().isCreated()).andReturn();
//        // minimum balance field is only present in checking accounts
//        assertTrue(result.getResponse().getContentAsString().contains("minimumBalance"));
//    }
//
//    @Test
//    @DisplayName("Test post request to create a student checking account when primary account holder is under 24")
//    void create_StudentCheckingAccount() throws Exception {
//        accountRequest.setPrimaryOwnerId(youngAccHolder.getId());
//        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andReturn();
//        // should create a student checking account which doesn't have a minimum balance
//        assertFalse(result.getResponse().getContentAsString().contains("minimumBalance"));
//    }
//
//    @Test
//    @DisplayName("Test post request to create a credit card with default values for creditLimit and interestRate")
//    void createCreditCard_DefaultValues() throws Exception {
//        accountRequest.setAccountType("credit-card");
//        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("\"creditLimit\":100"));
//        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.2"));
//    }
//
//    @Test
//    @DisplayName("Test post request to create a credit card with chosen values for creditLimit and interestRate")
//    void createCreditCard_ChosenValues() throws Exception {
//        accountRequest.setAccountType("credit-card");
//        accountRequest.setCreditCardLimit(new BigDecimal("200"));
//        accountRequest.setCardInterestRate(new BigDecimal("0.15"));
//
//        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("\"creditLimit\":200"));
//        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.15"));
//    }
//
//    @Test
//    @DisplayName("Test post request to create a credit card with value above limit for creditLimit, expected 400 status code")
//    void createCreditCard_InvalidCreditLimit() throws Exception {
//        accountRequest.setAccountType("credit-card");
//        accountRequest.setCreditCardLimit(new BigDecimal("10"));
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test post request to create a savings account with default values for minimumBalance and interestRate")
//    void createSavingsAccount_DefaultValues() throws Exception {
//        accountRequest.setAccountType("savings");
//
//        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.0025"));
//        assertTrue(result.getResponse().getContentAsString().contains("\"minimumBalance\":1000"));
//    }
//
//    @Test
//    @DisplayName("Test post request to create a savings account with chosen values for minimumBalance and interestRate")
//    void createSavingsAccount_ChosenValues() throws Exception {
//        accountRequest.setAccountType("savings");
//        accountRequest.setSavingsInterestRate(new BigDecimal("0.4"));
//        accountRequest.setSavingsMinimumBalance(new BigDecimal("800"));
//
//        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("\"interestRate\":0.4"));
//        assertTrue(result.getResponse().getContentAsString().contains("\"minimumBalance\":800"));
//    }
//
//    @Test
//    @DisplayName("Test post request to create a savings account with value above limit for interestRate, expected 400 status code")
//    void createSavingsAccount_InvalidInterestRate() throws Exception {
//        accountRequest.setAccountType("savings");
//        accountRequest.setSavingsInterestRate(new BigDecimal("10"));
//
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test post request to create a savings account with balance below minimum balance, expected 400 status code")
//    void createSavingsAccount_InvalidBalance() throws Exception {
//        accountRequest.setAccountType("savings");
//        accountRequest.setBalance(new Money(new BigDecimal("800")));
//
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test post request to create a checking account with balance below minimum balance, expected 400 status code")
//    void createCheckingAccount_InvalidBalance() throws Exception {
//        accountRequest.setBalance(new Money(new BigDecimal("100")));
//
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test post request to create a checking account without secret key, expected 400 status code")
//    void createCheckingAccount_NoSecretKey() throws Exception {
//        accountRequest.setSecretKey("");
//
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    // test invalid POST requests (wrong owner id || wrong account type)
//    @Test
//    @DisplayName("Test post request with wrong primary owner id, expected 400 status code")
//    void create_invalidPrimaryOwnerId() throws Exception {
//        accountRequest.setPrimaryOwnerId(32);
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test post request with wrong secondary owner id, expected 400 status code")
//    void create_invalidSecondaryOwnerId() throws Exception {
//        accountRequest.setSecondaryOwnerId(20);
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test post request with wrong account type, expected 400 status code")
//    void create_invalidAccountType() throws Exception {
//        accountRequest.setAccountType("student");
//        mockMvc.perform(post("/api/v1/accounts")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(accountRequest))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    /* TEST ADMIN DEBIT & CREDIT ACTIONS */
//    @Test
//    @DisplayName("Test debit checking account, expected reduced balance")
//    void financeAccount_DebitCheckingAccount() throws Exception {
//        mockMvc.perform(post("/api/v1/accounts/" + checkingAccount.getId())
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(debitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("1900.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test credit savings account, expected increased balance")
//    void financeAccount_CreditSavingsAccount() throws Exception {
//        mockMvc.perform(post("/api/v1/accounts/" + savingsAccount.getId())
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(creditRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("1200.00"), savingsAccountRepository.findById(savingsAccount.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test debit credit card, expected reduced balance")
//    void financeAccount_DebitCreditCard() throws Exception {
//        mockMvc.perform(post("/api/v1/accounts/" + creditCard.getId())
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(debitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("3900.00"), creditCardRepository.findById(creditCard.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test debit student account, expected reduced balance")
//    void financeAccount_DebitStudentAccount() throws Exception {
//        mockMvc.perform(post("/api/v1/accounts/" + studentAccount.getId())
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(debitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("2900.00"), studentAccountRepository.findById(studentAccount.getId()).get().getBalance().getAmount());
//    }
//
//
//    // invalid debit/credit requests
//    @Test
//    @DisplayName("Test debit/credit an account that doesn't exist, expected expected 400 status code")
//    void financeAccount_InvalidAccountId() throws Exception {
//        mockMvc.perform(post("/api/v1/accounts/20")
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(debitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    @DisplayName("Test finance account with invalid operation type, expected expected 400 status code")
//    void financeAccount_InvalidOperationType() throws Exception {
//        FinanceAdminRequest invalidRequest = new FinanceAdminRequest(new BigDecimal("200"), "finance");
//
//        mockMvc.perform(post("/api/v1/accounts/" + checkingAccount.getId())
//                .with(user("admin").roles("ADMIN"))
//                .content(objectMapper.writeValueAsString(invalidRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//
//    }
//
//    /* TEST PATCH REQUEST - unfreeze account by an admin*/
//    @Test
//    @DisplayName("Test patch request to unfreeze savings account")
//    void unfreezeAccount_SavingsAccount() throws Exception {
//        savingsAccount.setStatus(Status.FROZEN);
//        savingsAccountRepository.save(savingsAccount);
//        mockMvc.perform(patch("/api/v1/accounts/"+ savingsAccount.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals(Status.ACTIVE, savingsAccountRepository.findById(savingsAccount.getId()).get().getStatus());
//    }
//
//    @Test
//    @DisplayName("Test patch request to unfreeze savings account already active, expected 400 status code")
//    void unfreezeAccount_SavingsAccount_InvalidRequest() throws Exception {
//        mockMvc.perform(patch("/api/v1/accounts/"+ savingsAccount.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test patch request to unfreeze checking account")
//    void unfreezeAccount_CheckingAccount() throws Exception {
//        checkingAccount.setStatus(Status.FROZEN);
//        checkingAccountRepository.save(checkingAccount);
//        mockMvc.perform(patch("/api/v1/accounts/"+ checkingAccount.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals(Status.ACTIVE, checkingAccountRepository.findById(checkingAccount.getId()).get().getStatus());
//    }
//
//    @Test
//    @DisplayName("Test patch request to unfreeze checking account already active, expected 400 status code")
//    void unfreezeAccount_CheckingAccount_InvalidRequest() throws Exception {
//        mockMvc.perform(patch("/api/v1/accounts/"+ checkingAccount.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test patch request to unfreeze student account")
//    void unfreezeAccount_StudentAccount() throws Exception {
//        studentAccount.setStatus(Status.FROZEN);
//        studentAccountRepository.save(studentAccount);
//        mockMvc.perform(patch("/api/v1/accounts/"+ studentAccount.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals(Status.ACTIVE, studentAccountRepository.findById(studentAccount.getId()).get().getStatus());
//    }
//
//    @Test
//    @DisplayName("Test patch request to unfreeze student account already active, expected 400 status code")
//    void unfreezeAccount_StudentAccount_InvalidRequest() throws Exception {
//        mockMvc.perform(patch("/api/v1/accounts/"+ studentAccount.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test patch request to unfreeze a credit card, expected 400 status code")
//    void unfreezeAccount_InvalidAccountId() throws Exception {
//        mockMvc.perform(patch("/api/v1/accounts/"+ creditCard.getId() +"/status")
//                .with(user("admin").roles("ADMIN"))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//
//    /* ACCOUNT HOLDER - get all user accounts */
//    @Test
//    @DisplayName("Test get request to retrieve every account from logged user")
//    void getAllUserAccounts() throws Exception {
//        mockMvc.perform(get("/api/v1/users/accounts")
//                .with(httpBasic("ana_s", "1234")))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Test get by id of a checking account from logged user")
//    void getById_UserCheckingAccount() throws Exception {
//        MvcResult result = mockMvc.perform(get("/api/v1/users/accounts/" + checkingAccount.getId())
//                .with(httpBasic("ana_s", "1234")))
//                .andExpect(status().isOk()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("Ana"));
//        assertTrue(result.getResponse().getContentAsString().contains("Checking account"));
//    }
//
//    @Test
//    @DisplayName("Test get by id of a student account from logged user")
//    void getById_UserStudentAccount() throws Exception {
//        MvcResult result = mockMvc.perform(get("/api/v1/users/accounts/" + studentAccount.getId())
//                .with(httpBasic("ana_s", "1234")))
//                .andExpect(status().isOk()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("Ana"));
//        assertTrue(result.getResponse().getContentAsString().contains("Student account"));
//    }
//
//    @Test
//    @DisplayName("Test get by id of a savings account from logged user")
//    void getById_UserSavingsAccount() throws Exception {
//        MvcResult result = mockMvc.perform(get("/api/v1/users/accounts/" + savingsAccount.getId())
//                .with(httpBasic("ana_s", "1234")))
//                .andExpect(status().isOk()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("Ana"));
//        assertTrue(result.getResponse().getContentAsString().contains("Savings account"));
//    }
//
//    @Test
//    @DisplayName("Test get by id of a credit card from logged user")
//    void getById_UserCreditCard() throws Exception {
//        MvcResult result = mockMvc.perform(get("/api/v1/users/accounts/" + creditCard.getId())
//                .with(httpBasic("ana_s", "1234")))
//                .andExpect(status().isOk()).andReturn();
//
//        assertTrue(result.getResponse().getContentAsString().contains("Ana"));
//        assertTrue(result.getResponse().getContentAsString().contains("Credit card"));
//    }
//
//    @Test
//    @DisplayName("Test get by id with an invalid account id when logged user, expected 400 status code")
//    void getById_InvalidId_LoggedUser() throws Exception {
//        mockMvc.perform(get("/api/v1/users/accounts/20")
//                .with(httpBasic("ana_s", "1234")))
//                .andExpect(status().isBadRequest());
//    }
//
//    /* TEST THIRD PARTY USERS DEBIT & CREDIT ACTIONS */
//    @Test
//    @DisplayName("Test debit checking account by a third party, expected reduced balance")
//    void financeAccount_DebitCheckingAccount_ThirdParty() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + checkingAccount.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyDebitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("1900.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
//    }
//
//    @Test
//    @DisplayName("Test credit savings account by a third party, expected increased balance")
//    void financeAccount_CreditSavingsAccount_ThirdParty() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + savingsAccount.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyCreditRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("1200.00"), savingsAccountRepository.findById(savingsAccount.getId()).get().getBalance().getAmount());
//    }
//
//
//    @Test
//    @DisplayName("Test debit student account by a third party, expected reduced balance")
//    void financeAccount_DebitStudentAccount_ThirdParty() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + studentAccount.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyDebitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
//
//        assertEquals( new BigDecimal("2900.00"), studentAccountRepository.findById(studentAccount.getId()).get().getBalance().getAmount());
//    }
//
//    // invalid debit/credit requests
//    @Test
//    @DisplayName("Test debit/credit an account that doesn't exist by a third party, expected expected 400 status code")
//    void financeAccount_InvalidAccountId_ThirdParty() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/20")
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyDebitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//
//    }
//    @Test
//    @DisplayName("Test debit credit card by a third party, expected 403 status code")
//    void financeAccount_DebitCreditCard_ThirdParty() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + creditCard.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyDebitRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
//    }
//
//    @Test
//    @DisplayName("Test debit checking account by a third party with invalid secret key, expected 400 status code")
//    void financeAccount_DebitCheckingAccount_InvalidSecretKey() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + checkingAccount.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyInvalidRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    @DisplayName("Test debit savings account by a third party with invalid secret key, expected 400 status code")
//    void financeAccount_CreditSavingsAccount_InvalidSecretKey() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + savingsAccount.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyInvalidRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Test debit student account by a third party with invalid secret key, expected 400 status code")
//    void financeAccount_DebitStudentAccount_InvalidSecretKey() throws Exception {
//        mockMvc.perform(post("/api/v1/third-parties/accounts/" + studentAccount.getId())
//                .with(user("company").roles("THIRDPARTY"))
//                .content(objectMapper.writeValueAsString(thirdPartyInvalidRequest))
//                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
//    }
}