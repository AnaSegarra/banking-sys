package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    @Query("SELECT cc FROM CreditCard cc WHERE (primary_owner = :id OR secondary_owner = :id) AND id = :accountId")
    CreditCard findAccountById(@Param("id") Integer userId, @Param("accountId") Integer accountId);
}
