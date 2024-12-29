package com.example.legalxbackend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

@Service
public class GcsService {

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${gcs.credentials.file-name}")
    private String credentialsFileName;

    private Storage storage;

    @PostConstruct
    public void initialize() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource(credentialsFileName).getInputStream()
        );

        this.storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique filename
        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // Create blob info
        BlobId blobId = BlobId.of(bucketName, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Upload file to GCS
        storage.create(blobInfo, file.getBytes());

        // Return the public URL
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, filename);
    }
}