package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CheckingAccountRepository;
import com.segarra.bankingsystem.repositories.StudentAccountRepository;
import com.segarra.bankingsystem.models.Address;
import com.segarra.bankingsystem.models.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CheckingAccountServiceTest {
    @Autowired
    private CheckingAccountService checkingAccountService;

    @MockBean
    private CheckingAccountRepository checkingAccountRepository;
    @MockBean
    private AccountHolderRepository accountHolderRepository;
    @MockBean
    private StudentAccountRepository studentAccountRepository;

    private List<CheckingAccount> checkingAccountList;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");

        CheckingAccount checkingAccount = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("2000")), "1234");
        CheckingAccount checkingAccount2 = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("5000")), "1234");
        checkingAccountList = Arrays.asList(checkingAccount, checkingAccount2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Unit test - retrieval of all checking accounts")
    void getAll() {
        when(checkingAccountRepository.findAll()).thenReturn(checkingAccountList);
        assertEquals(2, checkingAccountService.getAll().size());
    }
}