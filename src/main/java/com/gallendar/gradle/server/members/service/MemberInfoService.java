package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.dto.MemberInfoResponse;
import com.gallendar.gradle.server.members.dto.MemberTagStatusRequest;
import com.gallendar.gradle.server.members.dto.MemberTagStatusResponse;
import com.gallendar.gradle.server.tags.domain.TagsRepositoryCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberInfoService {
    private final TagsRepositoryCustomImpl tagsRepositoryCustom;
    private final AuthenticationImpl authentication;

    public MemberInfoResponse myInfoGetById(String token) {
        Members members = authentication.getMemberByToken(token);
        return new MemberInfoResponse(members.getId(), members.getEmail());
    }

    public Page<MemberTagStatusResponse> mySharedStatusGetById(String token, MemberTagStatusRequest memberTagStatusRequest, Pageable pageable) {
        Members members = authentication.getMemberByToken(token);

        List<Board> tagStatus = tagsRepositoryCustom.getSharedStatusById(members.getId(), memberTagStatusRequest, pageable);
        List<MemberTagStatusResponse> list = setMemberTagStatusResponse(tagStatus);

        return new PageImpl<>(list);
    }

    private List<MemberTagStatusResponse> setMemberTagStatusResponse(List<Board> tagStatus) {
        List<MemberTagStatusResponse> list = new ArrayList<>();
        tagStatus.forEach(board -> {
            String to = board.getMembers().getId();
            String title = board.getTitle();
            board.getBoardTags().forEach(boardTags -> {
                list.add(MemberTagStatusResponse.from(to, title, boardTags.getTags().getTagsMember(), boardTags.getTags().getStatus(), boardTags.getTags().getUpdatedAt()));
            });
        });
        return list;
    }
}
