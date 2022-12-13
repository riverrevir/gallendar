package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;

import static com.gallendar.gradle.server.global.common.ErrorCode.MEMBER_NOT_FOUND;

public class MemberSearchImpl implements MemberSearch{
    private final MembersRepository membersRepository;

    public MemberSearchImpl(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    @Override
    public Members memberFindById(String userId) {
        return membersRepository.findById(userId).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND));
    }
}
