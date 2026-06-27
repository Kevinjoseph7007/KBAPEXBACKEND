package com.example.KBapexbackend_java.service.Impl;

import com.example.KBapexbackend_java.service.WhatsAppService;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class WhatsAppServiceImpl  implements WhatsAppService {
    @Value("${whatsapp.api.url}")
    private String WhatsAppAPIURL;
    @Value("${whatsapp.access.token}")
    private String AccessToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String sendMessage(String phone, String message){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body  = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", phone);
        body.put("type", "template");

        Map<String, Object> template = new HashMap<>();
        template.put("name", "kbapexmarketingtemplate");
        Map<String,Object> language = new HashMap<>();
        language.put("code", "en");
        template.put("language", language);
        body.put("template", template);
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(WhatsAppAPIURL, request, String.class);
        return extractMessageId(response.getBody());
    }

    // The send response looks like {"messages":[{"id":"wamid.XXX"}]}; pull out the wamid
    // so callers can persist it and later correlate inbound replies via their context.id.
    private String extractMessageId(String responseBody) {
        if (responseBody == null) {
            return null;
        }
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode messages = root.path("messages");
            if (messages.isArray() && messages.size() > 0) {
                return messages.get(0).path("id").asText(null);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
