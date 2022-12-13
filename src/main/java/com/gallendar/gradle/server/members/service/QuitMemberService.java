package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.global.auth.jwt.JwtUtils;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import com.gallendar.gradle.server.tags.domain.BoardTagsRepository;
import com.gallendar.gradle.server.tags.domain.Tags;
import com.gallendar.gradle.server.tags.domain.TagsRepository;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.gallendar.gradle.server.global.common.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuitMemberService {
    private final AuthenticationImpl authentication;
    private final BoardRepositoryCustomImpl boardRepositoryCustom;
    private final BoardRepository boardRepository;
    private final TagsRepository tagsRepository;
    private final BoardTagsRepository boardTagsRepository;
    private final MembersRepository membersRepository;

    @Transactional
    public ResponseEntity<?> quitMemberById(String token) {
        Members members = authentication.getMemberByToken(token);

        List<Board> boards = boardRepositoryCustom.findAllBoardById(members.getId());
        List<String> tagMember = getTagMember(boards);
        changeTagsMemberStatus(tagMember, members.getId());

        membersRepository.delete(members);
        HttpHeaders httpHeaders = setAuthorizationNull();

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    private HttpHeaders setAuthorizationNull() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, null);
        return httpHeaders;
    }

    private List<String> getTagMember(List<Board> boards) {
        List<String> tagMember = new ArrayList<>();
        boards.forEach(board -> {
            board.getBoardTags().forEach(boardTags -> {
                tagMember.add(boardTags.getTags().getTagsMember());
                tagsRepository.delete(boardTags.getTags());
                boardTagsRepository.delete(boardTags);
            });
            boardRepository.delete(board);
        });
        return tagMember;
    }

    private void changeTagsMemberStatus(List<String> tagMember, String memberId) {
        tagMember.forEach(t -> {
            Tags tags = tagsRepository.findByTagsMember(memberId);
            tags.changeTagsMember(TagStatus.quitMember);
        });
    }
}
