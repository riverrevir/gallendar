package com.gallendar.gradle.server.board.controller;

import com.gallendar.gradle.server.board.dto.*;
import com.gallendar.gradle.server.board.service.BoardCountService;
import com.gallendar.gradle.server.board.service.BoardCreateService;
import com.gallendar.gradle.server.board.service.BoardSearchService;
import com.gallendar.gradle.server.board.service.BoardUpdateService;
import com.gallendar.gradle.server.global.auth.jwt.JwtRequestFilter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
@Slf4j
public class BoardController {
    private final BoardCreateService boardCreateService;
    private final BoardSearchService boardSearchService;
    private final BoardCountService boardCountService;
    private final BoardUpdateService boardUpdateService;

    /**
     * 게시글 작성
     *
     * @param boardCreateRequest
     * @param token
     * @return
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity save(@Validated BoardCreateRequest boardCreateRequest, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) throws IOException {
        boardCreateService.save(boardCreateRequest, token);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 게시글 수정
     *
     * @param boardId
     * @param boardUpdateRequest
     * @param token
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") @Positive Long boardId,
                                 BoardUpdateRequest boardUpdateRequest, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) throws IOException {
        boardUpdateService.update(boardId, boardUpdateRequest, token);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 게시글 삭제
     *
     * @param boardId
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long boardId, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        boardService.delete(boardId, token);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 캘린더 조회
     *
     * @param year
     * @param month
     * @param category
     * @return
     */
    @GetMapping
    @ApiOperation(value = "캘린더 조회", notes = "(year, month, category) request 받아서 검색")
    public List<BoardSearchResponse> boardSearchByYearAndMonthAndCategory(@RequestParam int year, @RequestParam int month, @RequestParam String category, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        return boardSearchService.SearchBoardByYearAndMonthAndCategory(year, month, category, token);
    }

    /**
     * 게시글 조회
     *
     * @param boardId
     * @param token
     * @return
     */
    @ApiOperation(value = "게시글 조회", notes = "boardId로 해당 게시글의 상세 내용을 볼 수 있다.")
    @GetMapping("/{id}")
    public List<BoardSearchByIdResponse> boardSearchByBoardId(@PathVariable(value = "id") Long boardId, @RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        return boardSearchService.SearchBoardByBoardId(boardId, token);
    }

    /**
     * 게시글 작성 여부 반환
     *
     * @param token
     * @param year
     * @param month
     * @param day
     * @return
     */
    @ApiOperation(value = "게시글 작성 여부 반환", notes = "false = 게시글 작성 불가능, true = 게시글 작성 가능")
    @GetMapping("/count")
    public BoardCountResponse boardCountById(@RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token, @RequestParam int year, @RequestParam int month, @RequestParam int day) {
        return boardCountService.countBoardById(token, year, month, day);
    }
}
