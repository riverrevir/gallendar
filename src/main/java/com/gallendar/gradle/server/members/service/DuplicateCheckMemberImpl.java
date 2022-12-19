package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.members.domain.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DuplicateCheckMemberImpl implements DuplicateCheckMember {
    private final MembersRepository membersRepository;

    @Override
    public boolean isMemberId(String id) {
        return membersRepository.existsById(id);
    }

    @Override
    public boolean isMemberEmail(String email) {
        return membersRepository.existsByEmail(email);
    }
}
