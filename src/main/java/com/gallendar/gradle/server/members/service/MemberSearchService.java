package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.common.CustomException;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import com.gallendar.gradle.server.members.domain.MembersRepositoryCustomImpl;
import com.gallendar.gradle.server.members.dto.FindIdByEmailResponse;
import com.gallendar.gradle.server.members.dto.MemberSearchResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.gallendar.gradle.server.common.ErrorCode.EMAIL_NOT_FOUND;

@Service
@AllArgsConstructor
@Slf4j
public class MemberSearchService {
    private final MembersRepositoryCustomImpl membersRepositoryCustom;
    private final MembersRepository membersRepository;

    public List<MemberSearchResponse> MemberSearchById(String id) {
        List<Members> members = membersRepositoryCustom.findByUser(id);
        return members.stream().map(MemberSearchResponse::from).collect(Collectors.toList());
    }

    public FindIdByEmailResponse idFindByEmail(String email) {
        Members members = membersRepository.findByEmail(email).orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));
        return new FindIdByEmailResponse(members.getId());
    }
}
