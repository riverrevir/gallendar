package com.gallendar.gradle.server.members.service;

public interface Authentication {
    void authPassword(String requestPassword,String memberPassword);
}
