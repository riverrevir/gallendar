package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardUpdateRequest;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.photo.entity.Photo;

public interface BoardUpdate {
    void updateIsNotNullPhoto(BoardUpdateRequest boardUpdateRequest, Members members, Category category, Photo photo);

    void updateIsNullPhoto(BoardUpdateRequest boardUpdateRequest, Members members, Category category);
}
