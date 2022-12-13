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
    @Transactional
    public void createMember(SignupRequestDto signupRequestDto) {
        final String id = signupRequestDto.getId();
        final String email = signupRequestDto.getEmail();

        CheckDuplicateMember(id,email);

        final String password= commonEncoder.encode(signupRequestDto.getPassword());
        signupRequestDto.setPassword(password);
        membersRepository.save(signupRequestDto.toEntity());
    }
    private void CheckDuplicateMember(String id,String email){
        if(membersRepository.existsById(id)||membersRepository.existsByEmail(email)){
            throw new CustomException(DUPLICATE_RESOURCE);
        }
    }
}
