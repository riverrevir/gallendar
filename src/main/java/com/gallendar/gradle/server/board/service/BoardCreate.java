package com.gallendar.gradle.server.board.service;


import com.gallendar.gradle.server.board.dto.BoardCreateRequest;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.photo.entity.Photo;

public interface BoardCreate {
    Board save(BoardCreateRequest boardCreateRequest, Members members, Category category, Photo photo);
}
