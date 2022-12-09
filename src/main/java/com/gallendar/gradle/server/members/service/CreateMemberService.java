package com.gallendar.gradle.server.members.service;


import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.global.auth.encoder.CommonEncoder;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import com.gallendar.gradle.server.members.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.gallendar.gradle.server.global.common.ErrorCode.DUPLICATE_RESOURCE;


@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMemberService {

    private final MembersRepository membersRepository;
    private final CommonEncoder commonEncoder;

    public void checkMemberIdDuplication(String id) {
        boolean flag = membersRepository.existsById(id);
        if (flag) {
            throw new CustomException(DUPLICATE_RESOURCE);
        }
    }


    public void checkMemberEmailDuplication(String email) {
        boolean flag = membersRepository.existsByEmail(email);
        if (flag) {
            throw new CustomException(DUPLICATE_RESOURCE);
        }
    }

    @Transactional
    public void createMember(SignupRequestDto signupRequestDto) {
        final String id = signupRequestDto.getId();
        final String email = signupRequestDto.getEmail();
        if (membersRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATE_RESOURCE);
        } else if (membersRepository.existsById(id)) {
            throw new CustomException(DUPLICATE_RESOURCE);
        } else {
            String password = signupRequestDto.getPassword();
            signupRequestDto.setPassword(commonEncoder.encode(password));
            membersRepository.save(signupRequestDto.toEntity());
        }
    }
}
