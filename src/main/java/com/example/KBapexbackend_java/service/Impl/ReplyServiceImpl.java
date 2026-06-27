package com.example.KBapexbackend_java.service.Impl;

import com.example.KBapexbackend_java.Repository.InboundMessageRepository;
import com.example.KBapexbackend_java.Repository.MessageRepository;
import com.example.KBapexbackend_java.dto.ReplierDto;
import com.example.KBapexbackend_java.dto.ReplyDto;
import com.example.KBapexbackend_java.model.InboundMessage;
import com.example.KBapexbackend_java.model.Messages;
import com.example.KBapexbackend_java.service.ReplyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final InboundMessageRepository inboundMessageRepository;
    private final MessageRepository messageRepository;

    public ReplyServiceImpl(InboundMessageRepository inboundMessageRepository, MessageRepository messageRepository) {
        this.inboundMessageRepository = inboundMessageRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<ReplyDto> getReplies() {
        List<InboundMessage> inbound = new ArrayList<>(inboundMessageRepository.findAll());
        inbound.sort(Comparator.comparingLong((InboundMessage m) -> parseTimestamp(m.getWhatsappTimestamp())).reversed());

        List<ReplyDto> replies = new ArrayList<>(inbound.size());
        for (InboundMessage message : inbound) {
            String originalText = null;
            String originalPhone = null;
            if (message.getContextMessageId() != null) {
                Optional<Messages> original = messageRepository.findByWhatsappMessageId(message.getContextMessageId());
                if (original.isPresent()) {
                    originalText = original.get().getMessageText();
                    originalPhone = original.get().getPhoneNumber();
                }
            }
            replies.add(ReplyDto.from(message, originalText, originalPhone));
        }
        return replies;
    }

    @Override
    public List<ReplierDto> getRepliers() {
        Map<String, List<InboundMessage>> bySender = new LinkedHashMap<>();
        for (InboundMessage message : inboundMessageRepository.findAll()) {
            String key = message.getFromNumber() != null ? message.getFromNumber() : "unknown";
            bySender.computeIfAbsent(key, k -> new ArrayList<>()).add(message);
        }

        List<ReplierDto> repliers = new ArrayList<>(bySender.size());
        for (Map.Entry<String, List<InboundMessage>> entry : bySender.entrySet()) {
            InboundMessage latest = entry.getValue().stream()
                    .max(Comparator.comparingLong(m -> parseTimestamp(m.getWhatsappTimestamp())))
                    .orElse(null);
            repliers.add(new ReplierDto(
                    entry.getKey(),
                    entry.getValue().size(),
                    latest != null ? latest.getMessageText() : null,
                    latest != null ? latest.getWhatsappTimestamp() : null));
        }
        repliers.sort(Comparator.comparingLong((ReplierDto r) -> parseTimestamp(r.lastTimestamp())).reversed());
        return repliers;
    }

    private static long parseTimestamp(String value) {
        if (value == null) {
            return 0L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
