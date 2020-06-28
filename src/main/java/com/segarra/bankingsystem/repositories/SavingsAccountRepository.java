package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Integer> {
    @Query("SELECT sa FROM SavingsAccount sa WHERE (primary_owner = :id OR secondary_owner = :id) AND id = :accountId")
    SavingsAccount findAccountById(@Param("id") Integer userId, @Param("accountId") Integer accountId);
}
