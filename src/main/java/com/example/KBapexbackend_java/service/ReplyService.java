package com.example.KBapexbackend_java.service;

import com.example.KBapexbackend_java.dto.ReplierDto;
import com.example.KBapexbackend_java.dto.ReplyDto;

import java.util.List;

public interface ReplyService {

    // All inbound replies, newest first, enriched with the outbound message they reply to (when known).
    List<ReplyDto> getReplies();

    // One entry per person who replied, with their reply count and most recent message.
    List<ReplierDto> getRepliers();
}
