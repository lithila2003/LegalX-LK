package com.example.legalxbackend.model;

import org.springframework.core.io.Resource;

public class WithFileType {
    private final Resource resource;
    private final String contentType;

    public WithFileType(Resource resource, String contentType) {
        this.resource = resource;
        this.contentType = contentType;
    }

    public Resource getResource() {
        return resource;
    }

    public String getContentType() {
        return contentType;
    }
}