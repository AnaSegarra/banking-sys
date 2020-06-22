package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.SavingsAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.SavingsAccountRepository;
import com.segarra.bankingsystem.utils.Address;
import com.segarra.bankingsystem.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SavingsAccountServiceTest {
    @Autowired
    private SavingsAccountService savingsAccountService;

    @MockBean
    private SavingsAccountRepository savingsAccountRepository;
    @MockBean
    private AccountHolderRepository accountHolderRepository;

    private List<SavingsAccount> savingsAccountList;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"));
        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"));

        SavingsAccount savingsAccount = new SavingsAccount(accountHolder2,
                new Money(new BigDecimal("2000")), new BigDecimal("0.15"), 1234, new BigDecimal("200"));
        SavingsAccount savingsAccount2 = new SavingsAccount(accountHolder,
                new Money(new BigDecimal("2000")), new BigDecimal("0.5"), 1234, new BigDecimal("800"));
        savingsAccountList = Arrays.asList(savingsAccount, savingsAccount2);
    }

    @Test
    @DisplayName("Unit test - retrieval of all savings accounts")
    void getAll() {
        when(savingsAccountRepository.findAll()).thenReturn(savingsAccountList);
        assertEquals(2, savingsAccountService.getAll().size());
    }
}