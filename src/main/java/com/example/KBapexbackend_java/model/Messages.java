package com.example.KBapexbackend_java.model;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private String messageText;

    private String status;

    private String failureReason;

    private int retryCount;

    public Messages() {}

    public Messages(String phoneNumber, String messageText, String status) {
        this.phoneNumber = phoneNumber;
        this.messageText = messageText;
        this.status = status;
    }

    public Long getId() { return id; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getMessageText() { return messageText; }

    public String getStatus() { return status; }

    public String getFailureReason() { return failureReason; }

    public int getRetryCount() { return retryCount; }

    public void setStatus(String status) { this.status = status; }

    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
}