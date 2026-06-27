package com.example.KBapexbackend_java.Repository;

import com.example.KBapexbackend_java.model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Messages, Long> {
    Optional<Messages> findByWhatsappMessageId(String whatsappMessageId);
}
