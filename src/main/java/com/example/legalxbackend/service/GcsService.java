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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public String uploadFile(MultipartFile file, String userId, String folderPath) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        logger.info("Starting file upload for user: {}", userId);

        try {
            String filename = userId + "/" + (folderPath != null ? folderPath + "/" : "") + file.getOriginalFilename();
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
        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(userId + "/"));

        for (Blob blob : blobs.iterateAll()) {
            fileNames.add(blob.getName().replace(userId + "/", "")); // Remove userId from response
        }
        return fileNames;
    }

    public void createFolder(String userId, String folderName) {
        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        String folderPath = userId + "/" + folderName + "/";
        BlobId blobId = BlobId.of(bucketName, folderPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, new byte[0]);
        logger.info("Folder {} created successfully for user {}", folderName, userId);
    }


    public boolean deleteFile(String userId, String fileName) {
        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

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

    public void renameFile(String userId, String oldFileName, String newFileName) {
        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        // Check if the file extensions are the same
        String oldFileExtension = oldFileName.substring(oldFileName.lastIndexOf('.'));
        String newFileExtension = newFileName.substring(newFileName.lastIndexOf('.'));
        if (!oldFileExtension.equals(newFileExtension)) {
            throw new IllegalArgumentException("File extensions must be the same");
        }

        String oldFilePath = userId + "/" + oldFileName;
        String newFilePath = userId + "/" + newFileName;

        BlobId oldBlobId = BlobId.of(bucketName, oldFilePath);
        Blob oldBlob = storage.get(oldBlobId);

        if (oldBlob == null) {
            throw new IllegalArgumentException("File not found: " + oldFileName);
        }

        try {
            BlobId newBlobId = BlobId.of(bucketName, newFilePath);
            storage.copy(Storage.CopyRequest.of(oldBlobId, newBlobId));
            storage.delete(oldBlobId);
            logger.info("Renamed file {} to {} for user {}", oldFileName, newFileName, userId);
        } catch (StorageException e) {
            logger.error("Failed to rename file {} to {} for user {}", oldFileName, newFileName, userId, e);
            throw new IllegalStateException("Failed to rename file: " + e.getMessage(), e);
        }
    }

    public void renameFolder(String userId, String oldFolderName, String newFolderName) {
        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        String oldFolderPath = userId + "/" + oldFolderName + "/";
        String newFolderPath = userId + "/" + newFolderName + "/";

        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(oldFolderPath));

        for (Blob blob : blobs.iterateAll()) {
            String newBlobName = newFolderPath + blob.getName().substring(oldFolderPath.length());
            BlobId newBlobId = BlobId.of(bucketName, newBlobName);
            storage.copy(Storage.CopyRequest.of(blob.getBlobId(), newBlobId));
            storage.delete(blob.getBlobId());
        }

        logger.info("Renamed folder {} to {} for user {}", oldFolderName, newFolderName, userId);
    }

    public boolean deleteFolder(String userId, String folderName) {
        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        String folderPath = userId + "/" + folderName + "/";
        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(folderPath));

        boolean allDeleted = true;
        for (Blob blob : blobs.iterateAll()) {
            boolean deleted = storage.delete(blob.getBlobId());
            if (!deleted) {
                allDeleted = false;
                logger.warn("Failed to delete blob: {}", blob.getName());
            }
        }

        if (allDeleted) {
            logger.info("Folder {} deleted successfully for user {}", folderName, userId);
        } else {
            logger.warn("Failed to delete some files in folder {} for user {}", folderName, userId);
        }

        return allDeleted;
    }

    public byte[] downloadFolder(String userId, String folderName) throws IOException {
        if (storage == null) {
            throw new IllegalStateException("GCS service not properly initialized");
        }

        String folderPath = userId + "/" + folderName + "/";
        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(folderPath));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (Blob blob : blobs.iterateAll()) {
                String fileName = blob.getName().substring(folderPath.length());
                zipOutputStream.putNextEntry(new ZipEntry(fileName));
                zipOutputStream.write(blob.getContent());
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            logger.error("Failed to create zip file for folder: {}", folderName, e);
            throw new IOException("Failed to create zip file: " + e.getMessage(), e);
        }

        return byteArrayOutputStream.toByteArray();
    }

}