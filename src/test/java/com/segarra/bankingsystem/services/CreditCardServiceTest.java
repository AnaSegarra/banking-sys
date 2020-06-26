package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.CreditCard;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.CreditCardRepository;
import com.segarra.bankingsystem.models.Address;
import com.segarra.bankingsystem.models.Money;
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
class CreditCardServiceTest {
    @Autowired
    private CreditCardService creditCardService;

    @MockBean
    private CreditCardRepository creditCardRepository;
    @MockBean
    private AccountHolderRepository accountHolderRepository;

    private List<CreditCard> creditCardList;

    @BeforeEach
    void setUp() {
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28200"), "1234", "gema_s");
        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"), "1234", "ana_s");

        CreditCard creditCard = new CreditCard(accountHolder2,
                new Money(new BigDecimal("2000")), new BigDecimal("200"), new BigDecimal("0.2"));
        CreditCard creditCard2 = new CreditCard(accountHolder2,
                new Money(new BigDecimal("5000")), new BigDecimal("300"), new BigDecimal("0.15"));
        creditCardList = Arrays.asList(creditCard, creditCard2);
    }

    @Test
    @DisplayName("Unit test - retrieval of all credit cards")
    void getAll() {
        when(creditCardRepository.findAll()).thenReturn(creditCardList);
        assertEquals(2, creditCardService.getAll().size());
    }
}