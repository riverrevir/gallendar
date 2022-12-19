package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardUpdateRequest;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.photo.entity.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardUpdateImpl implements BoardUpdate {
    private final BoardRepository boardRepository;

    @Override
    public void updateIsNotNullPhoto(BoardUpdateRequest boardUpdateRequest, Members members, Category category, Photo photo) {
        Board board = boardUpdateRequest.toEntity();
        board.setMembers(members);
        board.setCategory(category);
        board.setPhoto(photo);
        boardRepository.save(board);
    }

    @Override
    public void updateIsNullPhoto(BoardUpdateRequest boardUpdateRequest, Members members, Category category) {
        Board board = boardUpdateRequest.toEntity();
        board.setMembers(members);
        board.setCategory(category);
        boardRepository.save(board);
    }
}
