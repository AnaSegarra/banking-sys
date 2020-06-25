package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.models.*;
import com.segarra.bankingsystem.repositories.*;
import com.segarra.bankingsystem.services.UserDetailsServiceImpl;
import com.segarra.bankingsystem.utils.Address;
import com.segarra.bankingsystem.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
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
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;
    private CheckingAccount checkingAccount;
    private CheckingAccount checkingAccount2;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
        AccountHolder youngAccHolder = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
                new Address("Spain", "Madrid", "Luna Avenue", 8, "28200"), "1234", "gabi_c");

        accountHolder.setPassword(passwordEncoder.encode(accountHolder.getPassword()));

        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2, youngAccHolder).collect(Collectors.toList()));


        Role role = new Role("ROLE_ACCOUNTHOLDER", accountHolder);
        roleRepository.save(role);

        SavingsAccount savingsAccount = new SavingsAccount(accountHolder2,
                new Money(new BigDecimal("2000")), new BigDecimal("0.15"), 1234, new BigDecimal("200"));
        SavingsAccount savingsAccount2 = new SavingsAccount(accountHolder,
                new Money(new BigDecimal("2000")), new BigDecimal("0.5"), 1234, new BigDecimal("800"));

        savingsAccountRepository.saveAll(Stream.of(savingsAccount, savingsAccount2).collect(Collectors.toList()));

        checkingAccount = new CheckingAccount(accountHolder,
                new Money(new BigDecimal("2000")), 1234);
        checkingAccount2 = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("5000")), 1234);

        checkingAccountRepository.saveAll(Stream.of(checkingAccount, checkingAccount2).collect(Collectors.toList()));

    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void makeTransaction() throws Exception {
        mockMvc.perform(post("/api/v1/transactions").param("recipient","checking")
                .param("sender", "checking")
                .with(httpBasic("ana_s", "1234"))
                .content("{\"recipientName\": \"Gema\", \"recipientId\":"+ checkingAccount2.getId() + ", \"senderId\":" + checkingAccount.getId() + ", \"amount\": 200}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals( new BigDecimal("1800.00"), checkingAccountRepository.findById(checkingAccount.getId()).get().getBalance().getAmount());
    }
}