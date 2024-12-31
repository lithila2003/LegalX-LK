package com.example.legalxbackend.service;

import com.example.legalxbackend.model.WithFileType;
import com.google.api.gax.paging.Page;
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
import java.util.ArrayList;
import java.util.List;

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
            // Use the original filename
            String filename = file.getOriginalFilename();

            // Set the content type for .docx files
            String contentType = file.getContentType();
            if (filename.endsWith(".docx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }

            // Create blob info
            BlobId blobId = BlobId.of(bucketName, filename);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
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

    public List<String> listFiles() {
        List<String> fileNames = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName);
        for (Blob blob : blobs.iterateAll()) {
            fileNames.add(blob.getName());
        }
        return fileNames;
    }

    public void deleteFile(String fileName) {
        logger.info("Attempting to delete file: {}", fileName);
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId);
        if (blob == null) {
            logger.warn("File {} not found in bucket {}", fileName, bucketName);
            return;
        }
        boolean deleted = storage.delete(blobId);
        if (deleted) {
            logger.info("File {} deleted successfully", fileName);
        } else {
            logger.warn("Failed to delete file {}", fileName);
        }
    }

    public WithFileType downloadFile(String fileName) {
        logger.info("Attempting to download file: {}", fileName);
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        if (blob == null) {
            logger.warn("File {} not found in bucket {}", fileName, bucketName);
            return null;
        }
        return new WithFileType(new ByteArrayResource(blob.getContent()), blob.getContentType());
    }
}