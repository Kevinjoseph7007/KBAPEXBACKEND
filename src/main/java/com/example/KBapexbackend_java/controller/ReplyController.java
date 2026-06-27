package com.example.KBapexbackend_java.controller;

import com.example.KBapexbackend_java.dto.ReplierDto;
import com.example.KBapexbackend_java.dto.ReplyDto;
import com.example.KBapexbackend_java.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/whatsApp/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping
    public ResponseEntity<List<ReplyDto>> getReplies() {
        return ResponseEntity.ok(replyService.getReplies());
    }

    @GetMapping("/people")
    public ResponseEntity<List<ReplierDto>> getRepliers() {
        return ResponseEntity.ok(replyService.getRepliers());
    }
}
