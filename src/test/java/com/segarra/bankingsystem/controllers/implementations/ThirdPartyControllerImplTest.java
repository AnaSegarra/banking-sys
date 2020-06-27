package com.segarra.bankingsystem.controllers.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.Address;
import com.segarra.bankingsystem.models.ThirdPartyUser;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.ThirdPartyRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerImplTest {
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ThirdPartyUser thirdParty;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
        thirdParty = new ThirdPartyUser("company1", "1234");
        ThirdPartyUser thirdParty2 = new ThirdPartyUser("company2", "1234");
        thirdPartyRepository.save(thirdParty2);
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
    }

    @Test
    @DisplayName("Test post request to create a new third party user")
    void create_validInput() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/third-parties")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(thirdParty))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("company1"));
    }


    @Test
    @DisplayName("Test post request to create third party user when username is taken, expected 400 status code")
    void create_InvalidUsername() throws Exception {
        thirdParty.setUsername("company2");
        mockMvc.perform(post("/api/v1/third-parties")
                .with(user("admin").roles("ADMIN"))
                .content(objectMapper.writeValueAsString(thirdParty))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}