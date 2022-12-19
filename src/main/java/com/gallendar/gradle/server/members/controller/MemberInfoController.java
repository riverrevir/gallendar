package com.gallendar.gradle.server.members.controller;

import com.gallendar.gradle.server.global.auth.jwt.JwtRequestFilter;
import com.gallendar.gradle.server.members.dto.MemberInfoResponse;
import com.gallendar.gradle.server.members.dto.MemberSearchResponse;
import com.gallendar.gradle.server.members.dto.MemberTagStatusRequest;
import com.gallendar.gradle.server.members.dto.MemberTagStatusResponse;
import com.gallendar.gradle.server.members.service.MemberInfoService;
import com.gallendar.gradle.server.members.service.MemberSearchService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
@Slf4j
public class MemberInfoController {
    private final MemberInfoService memberInfoService;
    private final MemberSearchService memberSearchService;

    /**
     * 나의 정보 요청
     *
     * @param token
     * @return
     */
    @ApiOperation(value = "나의 정보 요청", notes = "아이디, 이메일 반환")
    @GetMapping("/myInfo")
    public MemberInfoResponse getMyInfoById(@RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token) {
        return memberInfoService.myInfoGetById(token);
    }

    /**
     * 태그 로그 조회
     *
     * @param token
     * @param memberTagStatusRequest
     * @param pageable
     * @return
     */
    @ApiOperation(value = "태그 로그 조회", notes = "누가 누구한테 공유 했는지와 상태를 반환")
    @GetMapping("/tag")
    public Page<MemberTagStatusResponse> getMySharedStatusById(@RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token, MemberTagStatusRequest memberTagStatusRequest, Pageable pageable) {
        return memberInfoService.mySharedStatusGetById(token, memberTagStatusRequest, pageable);
    }

    /**
     * 유저 찾기(태그 추가할 때 사용)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "유저 찾기", notes = "유저의 id 값으로 요청이 들어오면 해당 요청이 포함된 모든 결과를 리스트로 반환한다.")
    @GetMapping
    public List<MemberSearchResponse> searchMemberById(@RequestParam(value = "separatorKey") @NotBlank String id) {
        return memberSearchService.memberSearchById(id);
    }
}
