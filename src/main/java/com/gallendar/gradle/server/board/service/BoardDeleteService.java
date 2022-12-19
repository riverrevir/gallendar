package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.category.service.CategoryDeleteImpl;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.service.AuthenticationImpl;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final AuthenticationImpl authentication;
    private final BoardSearchImpl boardSearch;
    private final CategoryDeleteImpl categoryDelete;
    private final BoardDeleteImpl boardDelete;

    public void delete(Long boardId, String token) {
        Members members = authentication.getMemberByToken(token);
        Board board = boardSearch.getBoardById(boardId);

        changeTagStatusDelete(board);
        categoryDelete.delete(board);
        boardDelete.delete(board);
    }

    private void changeTagStatusDelete(Board board) {
        board.getBoardTags().forEach(boardTag -> {
            if (boardTag.getTags().getStatus() == TagStatus.alert) {
                boardTag.getTags().changeStatus(TagStatus.delete);
            }
        });
    }
}
