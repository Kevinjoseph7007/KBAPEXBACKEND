package com.example.KBapexbackend_java.dto;

import com.example.KBapexbackend_java.model.InboundMessage;

public record ReplyDto(
        Long id,
        String fromNumber,
        String messageText,
        String messageType,
        String whatsappTimestamp,
        String contextMessageId,
        String originalMessageText,
        String originalPhoneNumber) {

    public static ReplyDto from(InboundMessage inbound, String originalMessageText, String originalPhoneNumber) {
        return new ReplyDto(
                inbound.getId(),
                inbound.getFromNumber(),
                inbound.getMessageText(),
                inbound.getMessageType(),
                inbound.getWhatsappTimestamp(),
                inbound.getContextMessageId(),
                originalMessageText,
                originalPhoneNumber);
    }
}
