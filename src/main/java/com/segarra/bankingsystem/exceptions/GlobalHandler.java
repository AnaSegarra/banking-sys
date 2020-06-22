package com.segarra.bankingsystem.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(ResourceNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalAccountTypeException.class)
    public void handleIllegalAccountTypeException(IllegalAccountTypeException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public void handleInsufficientFundsException(InsufficientFundsException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NotOwnerException.class)
    public void handleNotOwnerException(NotOwnerException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
    }
}
