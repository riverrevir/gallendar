package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.members.domain.Members;

public interface MemberSearch {
    Members memberFindById(String userId);
}
