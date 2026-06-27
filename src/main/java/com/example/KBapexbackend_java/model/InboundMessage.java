package com.example.KBapexbackend_java.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inbound_messages")
public class InboundMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The wamid of the inbound message itself
    private String whatsappMessageId;

    private String fromNumber;

    private String messageType;

    @Column(columnDefinition = "TEXT")
    private String messageText;

    // The wamid of the outbound message this is a reply to (from context.id), if any
    private String contextMessageId;

    private String whatsappTimestamp;

    public InboundMessage() {}

    public InboundMessage(String whatsappMessageId, String fromNumber, String messageType,
                          String messageText, String contextMessageId, String whatsappTimestamp) {
        this.whatsappMessageId = whatsappMessageId;
        this.fromNumber = fromNumber;
        this.messageType = messageType;
        this.messageText = messageText;
        this.contextMessageId = contextMessageId;
        this.whatsappTimestamp = whatsappTimestamp;
    }

    public Long getId() { return id; }

    public String getWhatsappMessageId() { return whatsappMessageId; }

    public String getFromNumber() { return fromNumber; }

    public String getMessageType() { return messageType; }

    public String getMessageText() { return messageText; }

    public String getContextMessageId() { return contextMessageId; }

    public String getWhatsappTimestamp() { return whatsappTimestamp; }

    public void setWhatsappMessageId(String whatsappMessageId) { this.whatsappMessageId = whatsappMessageId; }

    public void setFromNumber(String fromNumber) { this.fromNumber = fromNumber; }

    public void setMessageType(String messageType) { this.messageType = messageType; }

    public void setMessageText(String messageText) { this.messageText = messageText; }

    public void setContextMessageId(String contextMessageId) { this.contextMessageId = contextMessageId; }

    public void setWhatsappTimestamp(String whatsappTimestamp) { this.whatsappTimestamp = whatsappTimestamp; }
}
