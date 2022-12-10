package com.gallendar.gradle.server.photo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoService {
    String upload(MultipartFile multipartFile) throws IOException;
}