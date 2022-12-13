package com.gallendar.gradle.server.members.controller;

import com.gallendar.gradle.server.exception.BusinessLogicException;
import com.gallendar.gradle.server.members.dto.*;
import com.gallendar.gradle.server.members.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Validated
public class MembersController {
    private final CreateMemberService createMemberService;
    private final ChangePasswordService changePasswordService;
    private final MemberSearchService memberSearchService;
    private final DuplicateCheckService duplicateCheckService;

    /**
     * 아이디 찾기
     *
     * @param email
     * @return
     */
    @ApiOperation(value = "아이디 찾기", notes = "가입한 이메일을 통해서 로그인 아이디를 찾을 수 있다.")
    @GetMapping("/find/id")
    public FindIdByEmailResponse findIdByEmail(@RequestParam("email") @NotBlank String email) {
        return memberSearchService.idFindByEmail(email);
    }

    /**
     * 비밀번호 변경
     *
     * @param changePasswordRequest
     * @return
     */
    @PatchMapping("/password")
    public ResponseEntity changePasswordById(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        changePasswordService.passwordChangeById(changePasswordRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 아이디 중복 확인
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "아이디 중복 확인", notes = "입력한 아이디가 이미 가입되어있는지 확인 할 수 있다.")
    @GetMapping("/duplicate/id/{id}")
    public ResponseEntity DuplicateCheckMemberById(@PathVariable @NotBlank String id) {
        duplicateCheckService.CheckDuplicateMemberById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 이메일 중복확인
     *
     * @param email
     * @return
     */
    @ApiOperation(value = "이메일 중복 확인", notes = "입력한 이메일이 이미 가입되어있는지 확인 할 수 있다.")
    @GetMapping("/duplicate/email/{email}")
    public ResponseEntity DuplicateCheckMemberByEmail(@PathVariable @NotBlank String email) {
        duplicateCheckService.CheckDuplicateMemberByEmail(email);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원가입
     *
     * @param signupRequestDto
     * @return
     */
    @ApiOperation(value = "회원가입", notes = "가입되어 있지 않은 아이디와 이메일 그리고 이메일 인증을 통해 회원가입을 할 수 있다.")
    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        createMemberService.createMember(signupRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}