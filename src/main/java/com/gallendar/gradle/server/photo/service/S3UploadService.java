package com.gallendar.gradle.server.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3UploadService implements PhotoService{
    @Value("${cloud.aws.s3.bucket}")
    private final String bucket;
    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException{
        final String fileName= UUID.randomUUID()+"-"+multipartFile.getOriginalFilename();
        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        amazonS3.putObject(bucket,fileName,multipartFile.getInputStream(),objectMetadata);
        final String path = amazonS3.getUrl(bucket,fileName).toString();
        return path;
    }
}
