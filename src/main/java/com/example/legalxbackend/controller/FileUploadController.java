package com.example.legalxbackend.controller;

import com.example.legalxbackend.model.WithFileType;
import com.example.legalxbackend.service.GcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("userId") String userId,
                                             @RequestParam(value = "folderPath", required = false) String folderPath) throws IOException {
        if (file == null || file.isEmpty()) {
            logger.error("Received empty or null file");
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        logger.info("Received file from user: {}", userId);
        String fileUrl = gcsService.uploadFile(file, userId, folderPath);
        return ResponseEntity.ok().body(fileUrl);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(@RequestParam("userId") String userId) {
        List<String> fileNames = gcsService.listFiles(userId);
        return ResponseEntity.ok().body(fileNames);
    }

    @PostMapping("/create-folder")
    public ResponseEntity<String> createFolder(@RequestParam("userId") String userId, @RequestParam("folderName") String folderName) {
        try {
            gcsService.createFolder(userId, folderName);
            return ResponseEntity.ok().body("Folder created successfully");
        } catch (IllegalStateException e) {
            logger.error("Service configuration error", e);
            return ResponseEntity.internalServerError().body("Service not properly configured");
        } catch (Exception e) {
            logger.error("Failed to create folder", e);
            return ResponseEntity.internalServerError().body("Failed to create folder: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("userId") String userId, @RequestParam("fileName") String fileName) {
        boolean isDeleted = gcsService.deleteFile(userId, fileName);

        if (isDeleted) {
            return ResponseEntity.ok().body("File deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or could not be deleted");
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

    @PutMapping("/rename-file")
    public ResponseEntity<String> renameFile(@RequestParam("userId") String userId,
                                             @RequestParam("oldFileName") String oldFileName,
                                             @RequestParam("newFileName") String newFileName) {
        try {
            gcsService.renameFile(userId, oldFileName, newFileName);
            return ResponseEntity.ok().body("File renamed successfully");
        } catch (IllegalArgumentException e) {
            logger.error("File not found", e);
            return ResponseEntity.badRequest().body("File not found: " + oldFileName);
        } catch (IllegalStateException e) {
            logger.error("Service configuration error", e);
            return ResponseEntity.internalServerError().body("Service not properly configured");
        } catch (Exception e) {
            logger.error("Failed to rename file", e);
            return ResponseEntity.internalServerError().body("Failed to rename file: " + e.getMessage());
        }
    }

    @PutMapping("/rename-folder")
    public ResponseEntity<String> renameFolder(@RequestParam("userId") String userId,
                                               @RequestParam("oldFolderName") String oldFolderName,
                                               @RequestParam("newFolderName") String newFolderName) {
        try {
            gcsService.renameFolder(userId, oldFolderName, newFolderName);
            return ResponseEntity.ok().body("Folder renamed successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Folder not found", e);
            return ResponseEntity.badRequest().body("Folder not found: " + oldFolderName);
        } catch (IllegalStateException e) {
            logger.error("Service configuration error", e);
            return ResponseEntity.internalServerError().body("Service not properly configured");
        } catch (Exception e) {
            logger.error("Failed to rename folder", e);
            return ResponseEntity.internalServerError().body("Failed to rename folder: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-folder")
    public ResponseEntity<String> deleteFolder(@RequestParam("userId") String userId, @RequestParam("folderName") String folderName) {
        boolean isDeleted = gcsService.deleteFolder(userId, folderName);

        if (isDeleted) {
            return ResponseEntity.ok().body("Folder deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Folder not found or could not be deleted");
        }
    }

    @GetMapping("/download-folder")
    public ResponseEntity<Resource> downloadFolder(@RequestParam("userId") String userId,
                                                   @RequestParam("folderName") String folderName) {
        try {
            byte[] zipContent = gcsService.downloadFolder(userId, folderName);
            ByteArrayResource resource = new ByteArrayResource(zipContent);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + folderName + ".zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            logger.error("Failed to download folder", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

}