package com.gallendar.gradle.server.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gallendar.gradle.server.photo.repository.PhotoRepository;
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
public class S3PhotoUploadImpl implements PhotoUpload {
    @Value("${cloud.aws.s3.bucket}")
    private final String bucket;
    private final AmazonS3 amazonS3;
    @Override
    public void upload(MultipartFile multipartFile) throws IOException{
        final String fileName= setFileName(multipartFile.getOriginalFilename());
        S3UploadObject(multipartFile,fileName);
    }

    @Override
    public String setFileName(String originFileName) {
        return UUID.randomUUID()+"-"+originFileName;
    }
    @Override
    public String getS3FileUrl(String fileName){
        return amazonS3.getUrl(bucket,fileName).toString();
    }
    private void S3UploadObject(MultipartFile multipartFile,String fileName) throws IOException {
        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        amazonS3.putObject(bucket,fileName,multipartFile.getInputStream(),objectMetadata);
    }
}
