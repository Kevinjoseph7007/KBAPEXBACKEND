package com.example.KBapexbackend_java.worker;

import com.example.KBapexbackend_java.Repository.MessageRepository;
import com.example.KBapexbackend_java.model.Messages;
import com.example.KBapexbackend_java.service.WhatsAppService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MessageWorkerService {
    private final WhatsAppService whatsAppService;
    private final MessageRepository messageRepository;
    public MessageWorkerService(WhatsAppService whatsAppService, MessageRepository messageRepository){
        this.whatsAppService = whatsAppService;
        this.messageRepository = messageRepository;
    }
    @Async
    public void processMsg(Messages msg){
        try{
            this.whatsAppService.sendMessage(msg.getPhoneNumber(),msg.getMessageText());
            msg.setStatus("SENT");
        }catch (Exception e){
            msg.setStatus("FAILED");
            msg.setFailureReason(e.getMessage());
        }
        messageRepository.save(msg);
    }
}
