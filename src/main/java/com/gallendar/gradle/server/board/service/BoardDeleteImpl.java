package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardDeleteImpl implements BoardDelete {
    public final BoardRepository boardRepository;

    @Override
    public void delete(Board board) {
        boardRepository.delete(board);
    }
}
