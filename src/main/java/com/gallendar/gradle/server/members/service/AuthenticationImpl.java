package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.auth.encoder.CommonEncoder;
import com.gallendar.gradle.server.global.common.CustomException;

import lombok.RequiredArgsConstructor;

import static com.gallendar.gradle.server.global.common.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
public class AuthenticationImpl implements Authentication{
    private final CommonEncoder commonEncoder;
    @Override
    public void authPassword(String requestPassword, String memberPassword) {
        if(!commonEncoder.matches(requestPassword,memberPassword)){
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }
}
