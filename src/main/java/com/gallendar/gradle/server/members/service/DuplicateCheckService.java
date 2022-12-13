package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.common.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.gallendar.gradle.server.global.common.ErrorCode.DUPLICATE_RESOURCE;

@Service
@RequiredArgsConstructor
@Slf4j
public class DuplicateCheckService {
    private final DuplicateCheckMemberImpl duplicateCheckMember;

    public void checkDuplicateMemberById(String id) {
        checkDuplicateMembers(duplicateCheckMember.isMemberId(id));
    }

    public void checkDuplicateMemberByEmail(String email) {
        checkDuplicateMembers(duplicateCheckMember.isMemberEmail(email));
    }

    private void checkDuplicateMembers(boolean isMember) {
        if (isMember) throw new CustomException(DUPLICATE_RESOURCE);
    }
}
