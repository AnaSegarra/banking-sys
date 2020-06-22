package com.segarra.bankingsystem.exceptions;

public class IllegalAccountTypeException extends RuntimeException{
    public IllegalAccountTypeException(String message) {
        super(message);
    }
}
