package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.dto.AccountHolderVM;
import com.segarra.bankingsystem.dto.ThirdPartyUserVM;
import com.segarra.bankingsystem.models.AccountHolder;
import com.segarra.bankingsystem.models.Address;
import com.segarra.bankingsystem.models.Role;
import com.segarra.bankingsystem.models.ThirdPartyUser;
import com.segarra.bankingsystem.repositories.AccountHolderRepository;
import com.segarra.bankingsystem.repositories.RoleRepository;
import com.segarra.bankingsystem.repositories.ThirdPartyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ThirdPartyServiceTest {

    @Autowired
    private ThirdPartyService thirdPartyService;

    @MockBean
    private ThirdPartyRepository thirdPartyRepository;
    @MockBean
    private RoleRepository roleRepository;


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Unit test - create new third party user")
    void create() {
        ThirdPartyUser thirdPartyUser = new ThirdPartyUser("company_1", "1234");
        Role role = new Role("ROLE_ACCOUNTHOLDER", thirdPartyUser);
        when(thirdPartyRepository.save(any(ThirdPartyUser.class))).thenReturn(thirdPartyUser);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        ThirdPartyUserVM savedThirdParty = thirdPartyService.create(thirdPartyUser);
        assertEquals("company_1", savedThirdParty.getUsername());
    }
}