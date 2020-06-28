package com.segarra.bankingsystem.controllers.interfaces;

import com.segarra.bankingsystem.dto.ThirdPartyUserVM;
import com.segarra.bankingsystem.models.ThirdPartyUser;

public interface ThirdPartyController {
    ThirdPartyUserVM create(ThirdPartyUser thirdPartyUser);
}
