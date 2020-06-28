package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Integer> {
    @Query("SELECT ca FROM CheckingAccount ca WHERE (primary_owner = :id OR secondary_owner = :id) AND id = :accountId")
    CheckingAccount findAccountById(@Param("id") Integer userId, @Param("accountId") Integer accountId);
}
