package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class MailService {
    @Autowired
    private final SendEmail sendEmail;

    public void sendAuthEmail(String email) throws Exception {
        sendEmail.send(email);
    }

    public void checkAuthNum(String key, String email){
        sendEmail.verifyEmail(key, email);
    }
}
