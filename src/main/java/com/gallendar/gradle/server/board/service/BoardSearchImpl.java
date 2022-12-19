package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardSearchByIdResponse;
import com.gallendar.gradle.server.board.dto.BoardSearchResponse;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.gallendar.gradle.server.global.common.ErrorCode.POST_NOT_FOUND;

@RequiredArgsConstructor
public class BoardSearchImpl implements BoardSearch {
    private final BoardRepository boardRepository;
    private final BoardRepositoryCustomImpl boardRepositoryCustom;

    @Override
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    @Override
    public List<BoardSearchResponse> simpleSearchBoard(int year, int month, String category, String memberId) {
        List<BoardSearchResponse> list = new ArrayList<>();
        List<Board> boards = boardRepositoryCustom.findByBoard(year, month, category, memberId);
        boards.forEach(board -> {
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            board.getBoardTags().forEach(boardTags -> {
                if (boardTags.getTags().getStatus().equals(TagStatus.shared)) {
                    atomicBoolean.set(true);
                }
            });
            list.add(BoardSearchResponse.from(board, atomicBoolean.get()));
        });
        return list;
    }

    @Override
    public List<BoardSearchByIdResponse> detailSearchBoardById(Long boardId, String memberId) {
        List<BoardSearchByIdResponse> list = new ArrayList<>();
        List<Board> boards = boardRepositoryCustom.findByBoardId(boardId, memberId);
        boards.forEach(board -> {
            List<String> tags = new ArrayList<>();
            board.getBoardTags().forEach(boardTags -> {
                tags.add(boardTags.getTags().getTagsMember());
            });
            list.add(BoardSearchByIdResponse.from(board, tags));
        });
        return list;
    }
}
