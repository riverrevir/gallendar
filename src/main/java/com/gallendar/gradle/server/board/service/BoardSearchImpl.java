package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import com.gallendar.gradle.server.global.common.CustomException;
import lombok.RequiredArgsConstructor;

import static com.gallendar.gradle.server.global.common.ErrorCode.POST_NOT_FOUND;

@RequiredArgsConstructor
public class BoardSearchImpl implements BoardSearch {
    private final BoardRepository boardRepository;

    @Override
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }
}
