package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.common.CustomException;
import com.gallendar.gradle.server.global.auth.encoder.CommonEncoder;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import com.gallendar.gradle.server.members.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.gallendar.gradle.server.common.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangePasswordService {
    private final MembersRepository membersRepository;
    private final CommonEncoder commonEncoder;

    @Transactional
    public void passwordChangeById(ChangePasswordRequest changePasswordRequest) {
        Members members = membersRepository.findById(changePasswordRequest.getId()).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        members.changePassword(commonEncoder.encode(changePasswordRequest.getPassword()));
    }
}
