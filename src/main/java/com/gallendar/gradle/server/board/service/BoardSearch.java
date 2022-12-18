package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.entity.Board;

public interface BoardSearch {
    Board getBoardById(Long boardId);
}
