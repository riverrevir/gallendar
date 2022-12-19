package com.gallendar.gradle.server.photo.service;

import com.gallendar.gradle.server.photo.entity.Photo;
import com.gallendar.gradle.server.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhotoSave {
    private final PhotoRepository photoRepository;

    public Photo save(String fileName,String url){
        Photo photo = Photo.builder().fileName(fileName).path(url).build();
        photoRepository.save(photo);
        return photo;
    }
}
