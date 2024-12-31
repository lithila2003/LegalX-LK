package com.example.legalxbackend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class GcsService {
    private static final Logger logger = LoggerFactory.getLogger(GcsService.class);

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${gcs.credentials.file-name}")
    private String credentialsFileName;

    private Storage storage;

    @PostConstruct
    public void initialize() throws IOException {
        try {
            logger.info("Initializing GCS service with credentials file: {}", credentialsFileName);
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ClassPathResource(credentialsFileName).getInputStream()
            );

            this.storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
            logger.info("GCS service initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize GCS service", e);
            throw new IOException("Failed to initialize GCS service: " + e.getMessage(), e);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        logger.info("Starting file upload: {}", file.getOriginalFilename());

        try {
            // Generate a unique filename
            String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // Create blob info
            BlobId blobId = BlobId.of(bucketName, filename);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // Upload file to GCS
            storage.create(blobInfo, file.getBytes());

            String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, filename);
            logger.info("File uploaded successfully. Public URL: {}", publicUrl);
            return publicUrl;

        } catch (IOException e) {
            logger.error("Failed to upload file: {}", file.getOriginalFilename(), e);
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    public Resource downloadFile(String fileName) {
        try {
            Blob blob = storage.get(bucketName, fileName);
            if (blob == null) {
                throw new StorageException(404, "File not found: " + fileName);
            }

            byte[] content = blob.getContent();
            return new ByteArrayResource(content);
        } catch (StorageException e) {
            logger.error("Error downloading file: {}", fileName, e);
            throw e;
        }
    }

    public void deleteFile(String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            boolean deleted = storage.delete(blobId);

            if (!deleted) {
                throw new StorageException(404, "File not found: " + fileName);
            }
        } catch (StorageException e) {
            logger.error("Error deleting file: {}", fileName, e);
            throw e;
        }
    }
}