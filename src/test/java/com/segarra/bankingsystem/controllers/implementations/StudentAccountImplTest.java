package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.StudentAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.StudentAccountRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class StudentAccountImplTest {
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        AccountHolder accountHolder = new AccountHolder("Gema", LocalDate.of(2000, 10, 20),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"));
        AccountHolder accountHolder2 = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
                new Address("Spain", "Madrid", "Luna Avenue", 8, "28700"));
        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));

        StudentAccount studentAccount = new StudentAccount(accountHolder,
                new Money(new BigDecimal("2000")), 1234);
        StudentAccount studentAccount2 = new StudentAccount(accountHolder2,
                new Money(new BigDecimal("5000")), 1234);

        studentAccountRepository.saveAll(Stream.of(studentAccount, studentAccount2).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
        studentAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get request to retrieve all student accounts")
    void getAll() throws Exception {
        mockMvc.perform(get("/student-accounts")).andExpect(status().isOk());
    }
}