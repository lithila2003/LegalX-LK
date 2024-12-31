package com.example.legalxbackend.controller;

import com.example.legalxbackend.model.WithFileType;
import com.example.legalxbackend.service.GcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
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

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        List<String> fileNames = gcsService.listFiles();
        return ResponseEntity.ok().body(fileNames);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        try {
            gcsService.deleteFile(fileName);
            return ResponseEntity.ok().body("File deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete file", e);
            return ResponseEntity.internalServerError().body("Failed to delete file: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        WithFileType file = gcsService.downloadFile(fileName);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getResource());
    }
}