package com.segarra.bankingsystem.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FinanceThirdPartyRequest {
    @DecimalMin(value = "0", message = "Transaction amount must be above 0", inclusive = false)
    private BigDecimal amount;
    @NotNull(message = "Operation type required: debit or credit")
    private String operation;
    @NotNull(message = "Account secret key is required")
    private int secretKey;

    public FinanceThirdPartyRequest() {
    }

    public FinanceThirdPartyRequest(BigDecimal amount, String operation, int secretKey) {
        this.amount = amount;
        this.operation = operation;
        this.secretKey = secretKey;
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

    public int getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(int secretKey) {
        this.secretKey = secretKey;
    }
}
