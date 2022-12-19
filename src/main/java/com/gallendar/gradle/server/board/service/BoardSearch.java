package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardSearchByIdResponse;
import com.gallendar.gradle.server.board.dto.BoardSearchResponse;
import com.gallendar.gradle.server.board.entity.Board;

import java.util.List;

public interface BoardSearch {
    Board getBoardById(Long boardId);
    List<BoardSearchResponse> simpleSearchBoard(int year, int month, String category, String memberId);
    List<BoardSearchByIdResponse> detailSearchBoardById(Long boardId, String memberId);
}
