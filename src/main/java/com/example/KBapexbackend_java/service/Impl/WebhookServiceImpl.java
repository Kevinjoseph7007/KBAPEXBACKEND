package com.example.KBapexbackend_java.service.Impl;

import com.example.KBapexbackend_java.Repository.InboundMessageRepository;
import com.example.KBapexbackend_java.Repository.MessageRepository;
import com.example.KBapexbackend_java.model.InboundMessage;
import com.example.KBapexbackend_java.model.Messages;
import com.example.KBapexbackend_java.service.WebhookService;
import tools.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebhookServiceImpl implements WebhookService {

    @Value("${whatsapp.verify.token}")
    private String verifyToken;

    private final InboundMessageRepository inboundMessageRepository;
    private final MessageRepository messageRepository;

    public WebhookServiceImpl(InboundMessageRepository inboundMessageRepository,
                              MessageRepository messageRepository) {
        this.inboundMessageRepository = inboundMessageRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public String verify(String mode, String token, String challenge) {
        if ("subscribe".equals(mode) && verifyToken != null && verifyToken.equals(token)) {
            return challenge;
        }
        return null;
    }

    @Override
    public void processInbound(JsonNode payload) {
        if (payload == null) {
            return;
        }
        for (JsonNode entry : payload.path("entry")) {
            for (JsonNode change : entry.path("changes")) {
                JsonNode value = change.path("value");
                handleMessages(value.path("messages"));
                handleStatuses(value.path("statuses"));
            }
        }
    }

    // Inbound replies from users.
    private void handleMessages(JsonNode messages) {
        if (!messages.isArray()) {
            return;
        }
        for (JsonNode message : messages) {
            InboundMessage inbound = new InboundMessage();
            inbound.setWhatsappMessageId(message.path("id").asText(null));
            inbound.setFromNumber(message.path("from").asText(null));
            inbound.setMessageType(message.path("type").asText(null));
            inbound.setMessageText(message.path("text").path("body").asText(null));
            inbound.setContextMessageId(message.path("context").path("id").asText(null));
            inbound.setWhatsappTimestamp(message.path("timestamp").asText(null));
            inboundMessageRepository.save(inbound);
        }
    }

    // Delivery/read/failed receipts for outbound messages we sent.
    private void handleStatuses(JsonNode statuses) {
        if (!statuses.isArray()) {
            return;
        }
        for (JsonNode statusNode : statuses) {
            String whatsappMessageId = statusNode.path("id").asText(null);
            String status = statusNode.path("status").asText(null);
            if (whatsappMessageId == null || status == null) {
                continue;
            }
            Optional<Messages> existing = messageRepository.findByWhatsappMessageId(whatsappMessageId);
            existing.ifPresent(msg -> {
                msg.setStatus(status.toUpperCase());
                messageRepository.save(msg);
            });
        }
    }
}
