package com.example.KBapexbackend_java.dto;

public record ReplierDto(
        String fromNumber,
        long replyCount,
        String lastMessageText,
        String lastTimestamp) {
}
