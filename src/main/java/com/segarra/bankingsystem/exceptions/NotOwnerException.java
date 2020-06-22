package com.segarra.bankingsystem.exceptions;

public class NotOwnerException extends RuntimeException{
    public NotOwnerException(String message) {
        super(message);
    }
}
