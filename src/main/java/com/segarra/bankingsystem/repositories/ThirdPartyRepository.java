package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.ThirdPartyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartyRepository extends JpaRepository<ThirdPartyUser, Integer> {
}
