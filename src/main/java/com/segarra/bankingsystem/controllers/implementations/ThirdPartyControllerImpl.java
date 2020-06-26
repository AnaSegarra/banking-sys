package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.ThirdPartyController;
import com.segarra.bankingsystem.models.ThirdPartyUser;
import com.segarra.bankingsystem.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ThirdPartyControllerImpl implements ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/third-parties")
    public ThirdPartyUser create(@Valid @RequestBody ThirdPartyUser thirdPartyUser) {
        return thirdPartyService.create(thirdPartyUser);
    }
}
