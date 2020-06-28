package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.ThirdPartyController;
import com.segarra.bankingsystem.dto.ThirdPartyUserVM;
import com.segarra.bankingsystem.models.ThirdPartyUser;
import com.segarra.bankingsystem.services.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ThirdPartyControllerImpl implements ThirdPartyController {
    @Autowired
    ThirdPartyService thirdPartyService;

    @PostMapping("/third-parties")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdPartyUserVM create(@RequestBody @Valid  ThirdPartyUser thirdPartyUser) {
        return thirdPartyService.create(thirdPartyUser);
    }
}
