package com.segarra.bankingsystem.controllers.implementations;

import com.segarra.bankingsystem.controllers.interfaces.StudentAccountController;
import com.segarra.bankingsystem.models.StudentAccount;
import com.segarra.bankingsystem.services.StudentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentAccountControllerImpl implements StudentAccountController {
    @Autowired
    private StudentAccountService studentAccountService;

    @GetMapping("/student-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentAccount> getAll() {
        return studentAccountService.getAll();
    }
}
