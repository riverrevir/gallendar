package com.gallendar.gradle.server.tags.service;

import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.tags.domain.Tags;

import java.util.List;

public interface TagMembers {
    void save(List<String> tags, Board board);
    Tags save(String memberId);
    void setBoardTags(Board board,Tags tags);
}
