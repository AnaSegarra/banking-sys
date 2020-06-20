package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Integer> {
}
