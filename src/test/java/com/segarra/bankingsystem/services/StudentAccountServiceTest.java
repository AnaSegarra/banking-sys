package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.StudentAccount;
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
import static org.mockito.Mockito.when;

@SpringBootTest
class StudentAccountServiceTest {
//    @Autowired
//    private StudentAccountService studentAccountService;
//
//    @MockBean
//    private StudentAccountRepository studentAccountRepository;
//
//    private List<StudentAccount> studentAccountList;
//
//
//    @BeforeEach
//    void setUp() {
//        AccountHolder accountHolder = new AccountHolder("Gema", LocalDate.of(2000, 10, 20),
//                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "gema_s");
//        AccountHolder accountHolder2 = new AccountHolder("Gabi", LocalDate.of(2017, 1, 10),
//                new Address("Spain", "Madrid", "Luna Avenue", 8, "28700"), "1234", "ana_s");
//
//        StudentAccount studentAccount = new StudentAccount(accountHolder,
//                new Money(new BigDecimal("2000")), "1234");
//        StudentAccount studentAccount2 = new StudentAccount(accountHolder2,
//                new Money(new BigDecimal("5000")), "1234");
//        studentAccountList = Arrays.asList(studentAccount, studentAccount2);
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    @DisplayName("Unit test - retrieval of all student accounts")
//    void getAll() {
//        when(studentAccountRepository.findAll()).thenReturn(studentAccountList);
//        assertEquals(2, studentAccountService.getAll().size());
//    }
}