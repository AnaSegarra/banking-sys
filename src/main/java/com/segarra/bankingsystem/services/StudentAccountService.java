package com.segarra.bankingsystem.services;

import com.segarra.bankingsystem.models.StudentAccount;
import com.segarra.bankingsystem.repositories.StudentAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentAccountService {
    @Autowired
    private StudentAccountRepository studentAccountRepository;

    @Secured({"ROLE_ADMIN"})
    public List<StudentAccount> getAll(){
        return studentAccountRepository.findAll();
    }
}
