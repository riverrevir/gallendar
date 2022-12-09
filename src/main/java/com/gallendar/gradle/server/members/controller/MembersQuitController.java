package com.gallendar.gradle.server.members.controller;


import com.gallendar.gradle.server.global.auth.jwt.JwtRequestFilter;
import com.gallendar.gradle.server.members.service.QuitMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MembersQuitController {
    private final QuitMemberService quitMemberService;

    @DeleteMapping("/quit")
    public ResponseEntity<?> memberQuitById(@RequestHeader(value = JwtRequestFilter.HEADER_KEY) String token){
        return quitMemberService.quitMemberById(token);
    }
}
