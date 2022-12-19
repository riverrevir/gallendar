package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardSearchByIdResponse;
import com.gallendar.gradle.server.board.dto.BoardSearchResponse;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.global.auth.jwt.JwtUtils;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.service.AuthenticationImpl;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardSearchService {
    private final AuthenticationImpl authentication;
    private final BoardSearchImpl boardSearch;

    @Transactional
    public List<BoardSearchResponse> SearchBoardByYearAndMonthAndCategory(int year, int month, String category, String token) {
        Members members = authentication.getMemberByToken(token);
        return boardSearch.simpleSearchBoard(year, month, category, members.getId());
    }

    @Transactional
    public List<BoardSearchByIdResponse> SearchBoardByBoardId(Long boardId, String token) {
        Members members = authentication.getMemberByToken(token);
        return boardSearch.detailSearchBoardById(boardId, members.getId());
    }
}
