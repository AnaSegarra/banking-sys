package com.segarra.bankingsystem.exceptions;

public class IllegalTransactionException extends RuntimeException{
    public IllegalTransactionException(String message) {
        super(message);
    }
}
