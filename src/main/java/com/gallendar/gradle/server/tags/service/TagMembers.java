package com.gallendar.gradle.server.tags.service;

import com.gallendar.gradle.server.board.entity.Board;

import java.util.List;

public interface TagMembers {
    void save(List<String> tags, Board board);
}
