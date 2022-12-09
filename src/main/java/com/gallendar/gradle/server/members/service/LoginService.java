package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.global.auth.encoder.CommonEncoder;
import com.gallendar.gradle.server.global.auth.jwt.JwtUtils;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import com.gallendar.gradle.server.members.dto.LoginRequest;
import com.gallendar.gradle.server.members.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gallendar.gradle.server.global.common.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final JwtUtils jwtUtils;
    private final MembersRepository membersRepository;
    private final CommonEncoder commonEncoder;

    public LoginResponse LoginMember(LoginRequest loginRequest) {
        Members members = membersRepository.findById(loginRequest.getId()).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        if (!commonEncoder.matches(loginRequest.getPassword(), members.getPassword())) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        final String token = jwtUtils.generateToken(loginRequest.getId());
        return new LoginResponse(token);
    }
}
