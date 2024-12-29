package com.example.legalxbackend.controller;

import com.example.legalxbackend.service.GcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private GcsService gcsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Received file: {}", file.getOriginalFilename());
            String publicUrl = gcsService.uploadFile(file);
            return ResponseEntity.ok(publicUrl);
        } catch (Exception e) {
            logger.error("Failed to upload file", e);
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }
}