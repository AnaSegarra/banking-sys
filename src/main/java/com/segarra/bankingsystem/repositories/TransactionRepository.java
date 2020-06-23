package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.Account;
import com.segarra.bankingsystem.models.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE sender_account_id = :accountId ORDER BY date DESC")
    List<Transaction> findLastTransaction(@Param("accountId") Account id, Pageable pageable);
}
