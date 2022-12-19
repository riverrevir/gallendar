package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardCreateRequest;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.photo.entity.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardCreateImpl implements BoardCreate{
    private final BoardRepository boardRepository;

    @Override
    public Board save(BoardCreateRequest boardCreateRequest, Members members, Category category, Photo photo) {
        Board board=boardCreateRequest.toEntity();
        board.setMembers(members);
        board.setCategory(category);
        board.setPhoto(photo);
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board copy(Board board,Members members) {
        Board copyBoard = Board.builder().title(board.getTitle()).content(board.getContent()).music(board.getMusic()).url(board.getUrl()).created(board.getCreated()).build();
        copyBoard.setMembers(members);
        copyBoard.setCategory(board.getCategory());
        copyBoard.setPhoto(board.getPhoto());
        boardRepository.save(copyBoard);
    }
}
