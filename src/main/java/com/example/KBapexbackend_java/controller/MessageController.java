package com.example.KBapexbackend_java.controller;

import com.example.KBapexbackend_java.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/whatsApp")
public class MessageController {
    private final FileService fileService;

    public MessageController(FileService fileService){
        this.fileService = fileService;
    }
    @GetMapping
    public ResponseEntity<String>checkServer(){
        return ResponseEntity.ok("Server is up and runing");
    }
    @PostMapping("/Marketing")
    public ResponseEntity<String> uploadFile( @RequestParam("file") MultipartFile file) throws Exception{
        fileService.processFile(file);
        return ResponseEntity.ok("File Uploaded and processing started");
    }
}
