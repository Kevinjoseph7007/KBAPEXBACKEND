package com.example.KBapexbackend_java.controller;

import com.example.KBapexbackend_java.service.WebhookService;
import tools.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsApp/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    // Meta calls this once to verify the webhook; echo back hub.challenge when the token matches.
    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String verifyToken,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {
        String result = webhookService.verify(mode, verifyToken, challenge);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Inbound events (user replies and delivery/read statuses) are POSTed here.
    @PostMapping
    public ResponseEntity<String> receive(@RequestBody JsonNode payload) {
        webhookService.processInbound(payload);
        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}
