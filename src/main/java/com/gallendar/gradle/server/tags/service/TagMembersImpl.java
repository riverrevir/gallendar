package com.gallendar.gradle.server.tags.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.tags.domain.BoardTags;
import com.gallendar.gradle.server.tags.domain.BoardTagsRepository;
import com.gallendar.gradle.server.tags.domain.Tags;
import com.gallendar.gradle.server.tags.domain.TagsRepository;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagMembersImpl implements TagMembers{
    private final BoardTagsRepository boardTagsRepository;
    private final TagsRepository tagsRepository;
    @Override
    public void save(List<String> tags, Board board) {
        if(!tags.isEmpty()) {
            tags.forEach(t -> {
                BoardTags boardTags = new BoardTags();
                Tags tag = Tags.builder()
                        .tagsMember(t)
                        .tagStatus(TagStatus.alert)
                        .build();
                boardTags.setBoard(board);
                boardTags.setTags(tag);
                boardTagsRepository.save(boardTags);
                tagsRepository.save(tag);
            });
        }
    }

    @Override
    public Tags save(String memberId) {
        Tags tags = Tags.builder().tagStatus(TagStatus.shared).tagsMember(memberId).build();
        tagsRepository.save(tags);
        return tags;
    }

    @Override
    public void setBoardTags(Board board, Tags tags) {
        BoardTags boardTags = new BoardTags();
        boardTags.setBoard(board);
        boardTags.setTags(tags);
        boardTagsRepository.save(boardTags);
    }
}
