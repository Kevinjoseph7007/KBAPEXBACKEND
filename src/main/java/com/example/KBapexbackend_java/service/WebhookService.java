package com.example.KBapexbackend_java.service;

import tools.jackson.databind.JsonNode;

public interface WebhookService {
    // Returns the challenge string when the verification request is valid, otherwise null.
    String verify(String mode, String verifyToken, String challenge);

    // Parses an inbound webhook payload, persisting replies and updating delivery statuses.
    void processInbound(JsonNode payload);
}
