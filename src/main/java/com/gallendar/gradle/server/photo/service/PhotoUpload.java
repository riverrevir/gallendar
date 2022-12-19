package com.gallendar.gradle.server.photo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoUpload {
    void upload(MultipartFile multipartFile) throws IOException;
    String setFileName(String originFileName);
    String getS3FileUrl(String fileName);
}