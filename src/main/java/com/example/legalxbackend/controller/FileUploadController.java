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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private GcsService gcsService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) throws IOException {
        if (file == null || file.isEmpty()) {
            logger.error("Received empty or null file");
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        logger.info("Received file from user: {}", userId);
        String fileUrl = gcsService.uploadFile(file, userId);
        return ResponseEntity.ok().body(fileUrl);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(@RequestParam("userId") String userId) {
        List<String> fileNames = gcsService.listFiles(userId);
        return ResponseEntity.ok().body(fileNames);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("userId") String userId, @RequestParam("fileName") String fileName) {
        boolean isDeleted = gcsService.deleteFile(userId, fileName);

        if (isDeleted) {
            return ResponseEntity.ok().body("File deleted successfully");
        } else {
            return ResponseEntity.status(404).body("File not found or could not be deleted");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("userId") String userId, @RequestParam("fileName") String fileName) {
        WithFileType file = gcsService.downloadFile(userId, fileName);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(file.getResource());
    }

}