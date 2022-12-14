package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardCreateRequest;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.service.CategorySearchImpl;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.service.AuthenticationImpl;
import com.gallendar.gradle.server.photo.entity.Photo;
import com.gallendar.gradle.server.photo.service.PhotoSave;
import com.gallendar.gradle.server.photo.service.S3PhotoUploadImpl;
import com.gallendar.gradle.server.tags.service.TagMembersImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardCreateService {
    private final AuthenticationImpl authentication;
    private final S3PhotoUploadImpl s3PhotoUpload;
    private final PhotoSave photoSave;
    private final CategorySearchImpl categorySearch;
    private final BoardCreateImpl boardCreate;
    private final TagMembersImpl tagMembers;

    public void save(BoardCreateRequest boardCreateRequest,String token) throws IOException {
        Members members=authentication.getMemberByToken(token);

        final String fileName = s3PhotoUpload.setFileName(boardCreateRequest.getPhoto().getOriginalFilename());
        s3PhotoUpload.upload(boardCreateRequest.getPhoto());
        final String url=s3PhotoUpload.getS3FileUrl(fileName);
        Photo photo=photoSave.save(fileName,url);

        Category category=categorySearch.getCategoryByCategoryTitle(boardCreateRequest.getCategoryTitle());

        Board board=boardCreate.save(boardCreateRequest,members,category,photo);
        tagMembers.save(boardCreateRequest.getTags(),board);
    }
}
