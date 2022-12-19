package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.gallendar.gradle.server.global.common.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MemberSearchImpl implements MemberSearch {
    private final MembersRepository membersRepository;

    @Override
    public Members memberFindById(String userId) {
        return membersRepository.findById(userId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    @Override
    public Members memberFindByEmail(String email) {
        return membersRepository.findByEmail(email).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }
}
