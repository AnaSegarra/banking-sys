package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.Account;
import com.segarra.bankingsystem.models.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT MAX(t.date) FROM Transaction t WHERE sender_account_id = :accountId")
    LocalDateTime findLastTransaction(@Param("accountId") Account id);

    @Query(value = "SELECT SUM(t.amount) FROM transactions t WHERE CAST(t.date AS DATE) != CAST(:currentDate AS DATE) && sender_account_id = :accountId GROUP BY CAST(t.date AS DATE), sender_account_id ORDER BY SUM(t.amount) DESC LIMIT 1",
            nativeQuery = true)
    BigDecimal findHighestDailyTransactionByCustomer(@Param("currentDate") LocalDateTime currentDate, @Param("accountId") Account id);

    @Query(value = "SELECT SUM(t.amount), sender_account_id FROM transactions t where CAST(t.date AS DATE) = CAST(:currentDate AS DATE) AND sender_account_id = :accountId ORDER BY SUM(t.amount) DESC LIMIT 1",
            nativeQuery = true)
    BigDecimal findTodayTotalTransactions(@Param("currentDate") LocalDateTime currentDate, @Param("accountId") Account id);
}
