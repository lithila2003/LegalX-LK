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

    @Value("legalx")
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

    public String uploadFile(MultipartFile file, String userId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        logger.info("Starting file upload for user: {}", userId);

        try {
            String filename = userId + "/" + file.getOriginalFilename();
            String contentType = file.getContentType();

            BlobId blobId = BlobId.of(bucketName, filename);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();

            storage.create(blobInfo, file.getBytes());

            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, filename);
            logger.info("File uploaded successfully for user: {}", userId);
            return fileUrl;

        } catch (IOException e) {
            logger.error("Failed to upload file for user: {}", userId, e);
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    public List<String> listFiles(String userId) {
        List<String> fileNames = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName);

        for (Blob blob : blobs.iterateAll()) {
            if (blob.getName().startsWith(userId + "/")) { // Filter files by userId
                fileNames.add(blob.getName().replace(userId + "/", "")); // Remove userId from response
            }
        }
        return fileNames;
    }


    public boolean deleteFile(String userId, String fileName) {
        String userFile = userId + "/" + fileName;
        BlobId blobId = BlobId.of(bucketName, userFile);
        Blob blob = storage.get(blobId);

        if (blob == null) {
            logger.warn("File {} not found for user {}", fileName, userId);
            return false; // Return false if file does not exist
        }

        boolean deleted = storage.delete(blobId);
        if (deleted) {
            logger.info("File {} deleted successfully for user {}", fileName, userId);
            return true;
        } else {
            logger.warn("Failed to delete file {} for user {}", fileName, userId);
            return false;
        }
    }



    public WithFileType downloadFile(String userId, String fileName) {
        String userFile = userId + "/" + fileName;
        Blob blob = storage.get(BlobId.of(bucketName, userFile));

        if (blob == null) {
            logger.warn("File {} not found for user {}", fileName, userId);
            return null;
        }

        return new WithFileType(new ByteArrayResource(blob.getContent()), blob.getContentType());
    }

}