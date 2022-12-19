package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardUpdateRequest;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.service.CategorySearchImpl;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.service.AuthenticationImpl;
import com.gallendar.gradle.server.photo.entity.Photo;
import com.gallendar.gradle.server.photo.service.PhotoSave;
import com.gallendar.gradle.server.photo.service.S3PhotoUploadImpl;
import com.gallendar.gradle.server.tags.domain.BoardTags;
import com.gallendar.gradle.server.tags.domain.BoardTagsRepository;
import com.gallendar.gradle.server.tags.domain.Tags;
import com.gallendar.gradle.server.tags.domain.TagsRepository;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardUpdateService {
    private final AuthenticationImpl authentication;
    private final BoardSearchImpl boardSearch;
    private final BoardRepositoryCustomImpl boardRepositoryCustom;
    private final TagsRepository tagsRepository;
    private final BoardTagsRepository boardTagsRepository;
    private final CategorySearchImpl categorySearch;
    private final S3PhotoUploadImpl s3PhotoUpload;
    private final PhotoSave photoSave;
    private final BoardUpdateImpl boardUpdate;

    public void update(Long boardId, BoardUpdateRequest boardUpdateRequest, String token) throws IOException {
        Members members = authentication.getMemberByToken(token);
        Board board = boardSearch.getBoardById(boardId);
        List<Board> oldTagsMember = boardRepositoryCustom.findByTagMembers(boardId);

        List<String> oldTagsList = getOldTagList(oldTagsMember);
        List<String> filterNoneMatchOldTagsMember = filterNoneMatchOldTagsMemberList(oldTagsList, boardUpdateRequest.getTags());
        List<String> filterNoneMatchNewTagsMember = filterNoneMatchNewTagsMemberList(oldTagsList, boardUpdateRequest.getTags());

        setOldTagsMemberStatus(filterNoneMatchOldTagsMember);
        setNewTagsMember(filterNoneMatchNewTagsMember, board);

        Category category = categorySearch.getCategoryByCategoryTitle(boardUpdateRequest.getCategoryTitle());

        if (!boardUpdateRequest.getPhoto().isEmpty()) {
            final String fileName = s3PhotoUpload.setFileName(boardUpdateRequest.getPhoto().getOriginalFilename());
            s3PhotoUpload.upload(boardUpdateRequest.getPhoto());
            final String url = s3PhotoUpload.getS3FileUrl(fileName);
            Photo photo = photoSave.save(fileName, url);
            boardUpdate.updateIsNotNullPhoto(boardUpdateRequest, members, category, photo);
        }
        else{
            boardUpdate.updateIsNullPhoto(boardUpdateRequest,members,category);
        }
    }

    private List<String> getOldTagList(List<Board> oldTagsMember) {
        List<String> oldTagsList = new ArrayList<>();
        oldTagsMember.forEach(board1 -> {
            board1.getBoardTags().forEach(boardTags -> {
                oldTagsList.add(boardTags.getTags().getTagsMember());
            });
        });
        return oldTagsList;
    }

    private List<String> filterNoneMatchOldTagsMemberList(List<String> oldTagsList, List<String> newTagsMember) {
        return oldTagsList.stream().filter(o -> newTagsMember.stream().noneMatch(Predicate.isEqual(o))).collect(Collectors.toList());
    }

    private List<String> filterNoneMatchNewTagsMemberList(List<String> oldTagsList, List<String> newTagsMember) {
        return newTagsMember.stream().filter(n -> oldTagsList.stream().noneMatch(Predicate.isEqual(n))).collect(Collectors.toList());
    }

    private void setOldTagsMemberStatus(List<String> filterOldTagsMember) {
        filterOldTagsMember.forEach(o -> {
            Tags tags = tagsRepository.findByTagsMember(o);
            if (tags.getStatus().equals(TagStatus.alert)) {
                tags.changeStatus(TagStatus.deleteTag);
            }
        });
    }

    private void setNewTagsMember(List<String> newTagsMember, Board board) {
        newTagsMember.forEach(n -> {
            BoardTags boardTags = new BoardTags();
            Tags tags = Tags.builder()
                    .tagsMember(n)
                    .tagStatus(TagStatus.alert)
                    .build();
            boardTags.setBoard(board);
            boardTags.setTags(tags);

            boardTagsRepository.save(boardTags);
            tagsRepository.save(tags);
        });
    }
}
