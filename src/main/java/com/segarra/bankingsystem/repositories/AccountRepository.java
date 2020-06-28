package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE primary_owner = :id OR secondary_owner = :id")
    List<Account> findAllUserAccounts(@Param("id") Integer id);

    @Query("SELECT a FROM Account a WHERE (primary_owner = :userId OR secondary_owner = :userId) AND id = :accountId")
    Account findUserAccountById(@Param("userId") Integer userId, @Param("accountId") Integer accountId);
}
