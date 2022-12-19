package com.gallendar.gradle.server.tags.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.board.service.BoardCreateImpl;
import com.gallendar.gradle.server.exception.Message;
import com.gallendar.gradle.server.exception.Status;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.service.AuthenticationImpl;
import com.gallendar.gradle.server.tags.domain.*;
import com.gallendar.gradle.server.tags.dto.NotificationResponse;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final TagsRepositoryCustomImpl tagsRepositoryCustom;
    private final BoardRepositoryCustomImpl boardRepositoryCustom;
    private final AuthenticationImpl authentication;
    private final BoardCreateImpl boardCreate;
    private final TagMembersImpl tagMembers;

    @Transactional
    public List<NotificationResponse> tagsFindById(String token) {
        Members members = authentication.getMemberByToken(token);
        return tagStatusCheck(members.getId());
    }

    @Transactional
    public ResponseEntity<Message> acceptTagBoard(String token, Long boardId) {
        Message message = new Message();
        message.setMessage("공유가 수락되었습니다.");
        message.setStatus(Status.OK);

        Members members = authentication.getMemberByToken(token);
        Board board = sharedWithTagStatusChange(boardId, members.getId());
        Board copyBoard = boardCreate.copy(board, members);
        Tags tags = tagMembers.save(board.getMembers().getId());
        tagMembers.setBoardTags(copyBoard, tags);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> denyTagBoard(String token, Long boardId) {
        Message message = new Message();
        message.setMessage("공유가 거절되었습니다.");
        message.setStatus(Status.OK);

        Members members = authentication.getMemberByToken(token);
        Board board = sharedWithTagStatusChange(boardId, members.getId());
        setDenyTagStatus(board);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private List<NotificationResponse> tagStatusCheck(String memberId) {
        List<NotificationResponse> list = new ArrayList<>();
        List<Tags> tags = tagsRepositoryCustom.findByTagsMember(memberId);
        tags.forEach(tags1 -> {
            tags1.getBoardTags().forEach(boardTags -> {
                list.add(NotificationResponse.from(boardTags));
            });
        });
        return list;
    }

    private Board sharedWithTagStatusChange(Long boardId, String memberId) {
        Board board = boardRepositoryCustom.findById(boardId, memberId);

        board.getBoardTags().forEach(boardTags -> {
            boardTags.getTags().changeStatus(TagStatus.accept);
        });
        return board;
    }

    private void setDenyTagStatus(Board board) {
        board.getBoardTags().forEach(boardTags -> {
            boardTags.getTags().changeStatus(TagStatus.deny);
        });
    }
}
