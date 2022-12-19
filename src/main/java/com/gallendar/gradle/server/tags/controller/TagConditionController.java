package com.gallendar.gradle.server.tags.controller;

import com.gallendar.gradle.server.tags.dto.CountBoardResponse;
import com.gallendar.gradle.server.tags.service.TagConditionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
@Slf4j
public class TagConditionController {
    private final TagConditionService tagConditionService;

    /**
     * 태그 조건
     *
     * @param id
     * @param year
     * @param month
     * @param day
     * @return
     */
    @ApiOperation(value = "태그 조건", notes = "태그 할 유저의 해당 날짜에 게시글의 개수를 반환")
    @GetMapping("/count/{id}/{year}/{month}/{day}")
    public CountBoardResponse countBoardByTagMember(@PathVariable String id, @PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        return tagConditionService.boardCountByTagMember(id, year, month, day);
    }
}