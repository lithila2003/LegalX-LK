package com.example.legalxbackend.controller;

import com.example.legalxbackend.service.GcsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private GcsService gcsService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                logger.error("Received empty or null file");
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            logger.info("Received file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
            String publicUrl = gcsService.uploadFile(file);
            return ResponseEntity.ok().body(publicUrl);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid request", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            logger.error("Service configuration error", e);
            return ResponseEntity.internalServerError().body("Service not properly configured");
        } catch (Exception e) {
            logger.error("Failed to upload file", e);
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }
}