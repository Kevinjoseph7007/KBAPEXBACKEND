package com.example.KBapexbackend_java.service.Impl;

import com.example.KBapexbackend_java.Repository.MessageRepository;
import com.example.KBapexbackend_java.model.Messages;
import com.example.KBapexbackend_java.service.FileService;
import com.example.KBapexbackend_java.utils.CsvParserUtil;
import com.example.KBapexbackend_java.worker.MessageWorkerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    private final MessageRepository  messageRepository;
    private final MessageWorkerService workerService;

    public FileServiceImpl(MessageRepository messageRepository, MessageWorkerService workerService){
        this.messageRepository = messageRepository;
        this.workerService = workerService;
    }

    public void processFile(MultipartFile file) throws Exception{
        List<String[]> rows = CsvParserUtil.parseCsv(file);
        for(String[] row:rows){
            String phone = row[0];
            String message = row[1];
            Messages msg = new Messages(phone,message,"PENDING");
            this.messageRepository.save(msg);
            this.workerService.processMsg(msg);
        }
    }

}
