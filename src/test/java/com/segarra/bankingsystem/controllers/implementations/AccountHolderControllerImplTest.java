package com.segarra.bankingsystem.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.utils.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AccountHolderControllerImplTest {
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        AccountHolder accountHolder = new AccountHolder("Ana", LocalDate.of(1994, 4, 16),
                new Address("Spain", "Madrid", "Madrid Avenue", 8, "28700"),
                new Address("Spain", "Sabadell", "Carrer de l'Estrella", 6, "08201"));
        AccountHolder accountHolder2 = new AccountHolder("Gema", LocalDate.of(1991, 10, 20),
                new Address("Spain", "Madrid", "Luna Avenue", 13, "28700"));

        accountHolderRepository.saveAll(Stream.of(accountHolder, accountHolder2).collect(Collectors.toList()));
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
    }

    @Test
    @DisplayName("Test get request to retrieve all account holders")
    void getAll() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test post request to create a new account holder")
    void create() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .content("{\"name\": \"Gabi\", \"birthday\":\"2017-01-10\", \"primaryAddress\": {" +
                        "\"country\": \"Spain\", \"city\": \"Madrid\", \"street\": \"Luna Avenue\", " +
                        "\"number\": 13, \"zipCode\": \"28700\"}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Madrid"));
    }
}