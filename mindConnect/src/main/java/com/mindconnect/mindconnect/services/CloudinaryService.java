package com.mindconnect.mindconnect.services;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    public String uploadFile(MultipartFile file);
}
