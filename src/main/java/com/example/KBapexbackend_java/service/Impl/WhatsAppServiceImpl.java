package com.example.KBapexbackend_java.service.Impl;

import com.example.KBapexbackend_java.service.WhatsAppService;
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
        return response.getBody();
    }
}
