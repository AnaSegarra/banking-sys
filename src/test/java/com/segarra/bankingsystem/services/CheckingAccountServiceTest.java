package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.CheckingAccountBody;
import com.segarra.bankingsystem.dto.CheckingAccountMV;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.models.StudentAccount;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CheckingAccountRepository;
import com.segarra.bankingsystem.repositories.StudentAccountRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private AccountHolder accountHolder;
    private AccountHolder youngAccHolder;


    @BeforeEach
    void setUp() {
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"));
        accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"));
        youngAccHolder = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
                new Address("Spain", "Madrid", "Luna Avenue", 8, "28200"));

        CheckingAccount checkingAccount = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("2000")), 1234);
        CheckingAccount checkingAccount2 = new CheckingAccount(accountHolder2,
                new Money(new BigDecimal("5000")), 1234);
        checkingAccountList = Arrays.asList(checkingAccount, checkingAccount2);
    }

    @Test
    @DisplayName("Unit test - retrieval of all checking accounts")
    void getAll() {
        when(checkingAccountRepository.findAll()).thenReturn(checkingAccountList);
        assertEquals(2, checkingAccountService.getAll().size());
    }

    @Test
    @DisplayName("Unit test - creation of checking account")
    void createCheckingAccount() {
        CheckingAccount checkingAccount = new CheckingAccount(accountHolder, new Money(new BigDecimal("12000")), 1234);
        when(checkingAccountRepository.save(any(CheckingAccount.class))).thenReturn(checkingAccount);
        when(accountHolderRepository.findById(1)).thenReturn(Optional.of(accountHolder));

        CheckingAccountMV savedCheckingAccount = checkingAccountService.create(new CheckingAccountBody(1, checkingAccount.getBalance(), checkingAccount.getSecretKey()));
        assertEquals(new Money(new BigDecimal("12000")).getAmount(), savedCheckingAccount.getCheckingAccount().getBalance().getAmount());
    }

    @Test
    @DisplayName("Unit test - creation of student checking account")
    void createStudentAccount() {
        StudentAccount studentAccount = new StudentAccount(youngAccHolder, new Money(new BigDecimal("6000")), 1234);
        when(studentAccountRepository.save(any(StudentAccount.class))).thenReturn(studentAccount);
        when(accountHolderRepository.findById(1)).thenReturn(Optional.of(youngAccHolder));

        CheckingAccountMV savedStudentAccount = checkingAccountService.create(new CheckingAccountBody(1, studentAccount.getBalance(), studentAccount.getSecretKey()));
        // primary owner under 24 results in creation of student account instead of normal checking account
        assertNull(savedStudentAccount.getCheckingAccount());
        assertEquals(new Money(new BigDecimal("6000")).getAmount(), savedStudentAccount.getStudentAccount().getBalance().getAmount());
    }
}