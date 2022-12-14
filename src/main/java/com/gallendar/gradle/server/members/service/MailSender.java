package com.gallendar.gradle.server.members.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface MailSender {
    void send(MimeMessage message,String email) throws Exception;
    MimeMessage createForm(String email) throws MessagingException;
    void verifyAuthNumber(String authNumber,String email);
}
