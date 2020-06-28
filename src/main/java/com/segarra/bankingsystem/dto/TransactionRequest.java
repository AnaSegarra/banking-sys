package com.segarra.bankingsystem.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class TransactionRequest {
    private String recipientName;
    @Min(value = 1, message = "Recipient account id must be a valid number greater thant 0")
    private int recipientId;
    @Min(value = 1, message = "Sender account id must be a valid number greater thant 0")
    private int senderId;
    @DecimalMin(value = "0", message = "Transaction amount must be above 0", inclusive = false)
    private BigDecimal amount;

    public TransactionRequest() {
    }

    public TransactionRequest(String recipientName, int recipientId, int senderId, BigDecimal amount) {
        this.recipientName = recipientName;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.amount = amount;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
