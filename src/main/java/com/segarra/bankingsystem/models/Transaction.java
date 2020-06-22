package com.segarra.bankingsystem.models;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate date;

    public Transaction() {
    }

    public Transaction(Account senderId, Account recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.date = LocalDate.now();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
