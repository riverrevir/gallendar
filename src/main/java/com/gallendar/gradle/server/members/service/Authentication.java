package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.members.domain.Members;

public interface Authentication {
    void authPassword(String requestPassword, String memberPassword);

    Members getMemberByToken(String token);
}
