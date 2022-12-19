package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.members.dto.AuthNumberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


@RequiredArgsConstructor
@Service
@Slf4j
public class MailService {
    private MailSenderImpl googleMail;

    public void sendAuthEmail(String email) throws Exception {
        MimeMessage message=googleMail.createForm(email);
        googleMail.send(message,email);
    }

    public void emailAuthByAuthNumber(AuthNumberRequest authNumberRequest){
        googleMail.verifyAuthNumber(authNumberRequest.getAuthNumber(),authNumberRequest.getEmail());
    }
}
