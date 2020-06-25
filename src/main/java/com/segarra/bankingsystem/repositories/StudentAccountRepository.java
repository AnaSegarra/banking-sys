package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Integer> {
    @Query("SELECT c FROM StudentAccount c WHERE primary_owner = :primaryOwnerId OR secondary_owner = :secondaryOwnerId ")
    List<StudentAccount> findByOwner(@Param("primaryOwnerId") int primaryOwnerId,
                                      @Param("secondaryOwnerId") int secondaryOwnerId);
}
