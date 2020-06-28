package com.segarra.bankingsystem.models;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderId;
    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    private Account recipientId;
    private LocalDateTime date;
    @DecimalMin(value = "0", message = "Transaction amount must be above 0", inclusive = false)
    private BigDecimal amount;

    public Transaction() {
        this.date = LocalDateTime.now();
    }

    public Transaction(Account senderId, Account recipientId, BigDecimal amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getSenderId() {
        return senderId;
    }

    public void setSenderId(Account senderId) {
        this.senderId = senderId;
    }

    public Account getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Account recipientId) {
        this.recipientId = recipientId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction {" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
