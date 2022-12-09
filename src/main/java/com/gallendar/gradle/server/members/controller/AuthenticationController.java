package com.gallendar.gradle.server.members.controller;

import com.gallendar.gradle.server.exception.BusinessLogicException;
import com.gallendar.gradle.server.members.dto.AuthNumDto;
import com.gallendar.gradle.server.members.dto.EmailRequest;
import com.gallendar.gradle.server.members.dto.LoginRequest;
import com.gallendar.gradle.server.members.dto.LoginResponse;
import com.gallendar.gradle.server.members.service.LoginService;
import com.gallendar.gradle.server.members.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;


@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
@Slf4j
public class AuthenticationController {


    private final MailService mailService;

    private final LoginService loginService;

    /**
     * 로그인
     *
     * @param loginRequest
     * @return
     */
    @ApiOperation(value = "로그인", notes = "등록된 회원이 로그인을 시도하여 성공하면 토큰을 응답, 실패하면 예외발생")
    @PostMapping
    public LoginResponse memberLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return loginService.LoginMember(loginRequest);
    }


    /**
     * 로그아웃
     *
     * @return
     */
    @ApiOperation(value = "로그아웃", notes = "헤더에 있는 토큰 값을 비운다.")
    @GetMapping
    public ResponseEntity<?> membersLogout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, null);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    /**
     * 인증번호 발송
     *
     * @param emailRequest
     * @throws Exception
     */
    @ApiOperation(value = "인증 번호 발송", notes = "가입하려하는 이메일로 인증번호를 발송한다.")
    @PostMapping("/email")
    public void sendAuthEmail(@Valid @RequestBody EmailRequest emailRequest) throws Exception {
        mailService.sendAuthEmail(emailRequest.getEmail());
    }

    /**
     * 인증번호 검증
     *
     * @param authNumDto
     * @return
     */
    @ApiOperation(value = "인증번호 검증", notes = "입력한 인증번호가 맞는지 확인한다.")
    @PostMapping("/email/verify")
    public ResponseEntity getEmailAuthenticationNumber(@Valid @RequestBody AuthNumDto authNumDto) {
        mailService.checkAuthNum(authNumDto.getAuthNum(), authNumDto.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }
}
