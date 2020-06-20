package com.segarra.bankingsystem.dto;

import com.segarra.bankingsystem.models.CheckingAccount;
import com.segarra.bankingsystem.models.StudentAccount;

public class CheckingAccountMV {
    private CheckingAccount checkingAccount;
    private StudentAccount studentAccount;

    public CheckingAccountMV(CheckingAccount checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public CheckingAccountMV(StudentAccount studentAccount) {
        this.studentAccount = studentAccount;
    }

    public CheckingAccount getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(CheckingAccount checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public StudentAccount getStudentAccount() {
        return studentAccount;
    }

    public void setStudentAccount(StudentAccount studentAccount) {
        this.studentAccount = studentAccount;
    }
}
