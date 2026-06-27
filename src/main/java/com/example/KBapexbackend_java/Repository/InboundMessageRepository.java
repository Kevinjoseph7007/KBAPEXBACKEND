package com.example.KBapexbackend_java.Repository;

import com.example.KBapexbackend_java.model.InboundMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InboundMessageRepository extends JpaRepository<InboundMessage, Long> {
    List<InboundMessage> findByContextMessageId(String contextMessageId);
    List<InboundMessage> findByFromNumber(String fromNumber);
}
