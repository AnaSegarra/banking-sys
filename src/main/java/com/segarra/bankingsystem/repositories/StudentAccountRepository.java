package com.segarra.bankingsystem.repositories;

import com.segarra.bankingsystem.models.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Integer> {
}
