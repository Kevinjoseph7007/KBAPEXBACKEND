package com.example.KBapexbackend_java.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void processFile(MultipartFile file) throws Exception;
}
