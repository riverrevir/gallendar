package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.auth.encoder.CommonEncoder;
import com.gallendar.gradle.server.global.auth.jwt.JwtUtils;
import com.gallendar.gradle.server.global.common.CustomException;

import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import lombok.RequiredArgsConstructor;

import static com.gallendar.gradle.server.global.common.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
public class AuthenticationImpl implements Authentication {
    private final CommonEncoder commonEncoder;
    private final JwtUtils jwtUtils;
    private final MembersRepository membersRepository;

    @Override
    public void authPassword(String requestPassword, String memberPassword) {
        if (!commonEncoder.matches(requestPassword, memberPassword)) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }

    @Override
    public Members getMemberByToken(String token) {
        final String id = jwtUtils.getMemberIdFromToken(token);
        Members members = membersRepository.findById(id).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return members;
    }
}
