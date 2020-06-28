package com.segarra.bankingsystem.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FinanceAdminRequest {
    @DecimalMin(value = "0", message = "Transaction amount must be above 0", inclusive = false)
    private BigDecimal amount;
    @NotNull(message = "Operation type required: debit or credit")
    private String operation;

    public FinanceAdminRequest() {
    }

    public FinanceAdminRequest(BigDecimal amount, String operation) {
        this.amount = amount;
        this.operation = operation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


}
