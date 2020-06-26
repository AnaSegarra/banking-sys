package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Integer> {
    @Query("SELECT s FROM StudentAccount s WHERE (primary_owner = :id OR secondary_owner = :id) AND id = :accountId")
    StudentAccount findAccountById(@Param("id") Integer userId, @Param("accountId") Integer accountId);
}
