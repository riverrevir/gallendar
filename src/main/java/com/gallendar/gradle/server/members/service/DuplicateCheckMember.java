package com.gallendar.gradle.server.members.service;

public interface DuplicateCheckMember {
    boolean isMemberId(String id);

    boolean isMemberEmail(String email);
}
