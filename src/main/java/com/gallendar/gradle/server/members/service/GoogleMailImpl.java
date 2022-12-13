package com.gallendar.gradle.server.members.service;

import com.gallendar.gradle.server.global.common.CustomException;
import com.gallendar.gradle.server.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.gallendar.gradle.server.global.common.ErrorCode.AUTH_NUMBER_MISS_MATCH;

@RequiredArgsConstructor
public class GoogleMailImpl implements GoogleMail {
    private final String authNum = CreateAuthNum.createNum();
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Override
    public void send(MimeMessage message,String email) throws Exception {
        mailSender.send(message);
        redisUtil.setDataExpire(authNum, email, 60 * 5L);
    }

    @Override
    public MimeMessage createForm(String email) throws MessagingException {
        final String setFrom = "5kamjas.gallender@gmail.com";
        final String title = "Gallendar 인증 번호";

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(setFrom);

        String content = "";
        content += "<div width='100%' style='margin:0;background-color:#fff'>";
        content += "<div style='margin:auto;max-width:600px;padding-top:50px' class='email-container'>";
        content += " <table role='presentation' cellspacing='0' cellpadding='0' width='100%' align='center' id='emailBodyContainer' style='border:0px;solid #d6d6d6;max-width:600px'>";
        content += "<tbody><tr>";
        content += "<td style='background-color:#fff;color:#444;font-family:\"Amazon Ember\",\"Helvetica Neue\",Roboto,Arial,sans-serif;font-size:14px;line-height:140%;padding:25px 35px'>";
        content += "<h1 style='font-size:20px;font-weight:bold;line-height:1.3;margin:0 0 15px 0'>이메일 주소 확인</h1>";
        content += "<p style='margin:0;padding:0'>안녕하세요, Gallendar입니다. 사용자가 본인임을 확인하기 위해 아래 인증번호를 입력해주세요.</p>";
        content += "<p style='margin:0;padding:0'></p>";
        content += "</td>";
        content += "</tr>";
        content += "<tr>";
        content += "<td style='background-color:#fff;color:#444;font-family:\"Amazon Ember\",\"Helvetica Neue\",Roboto,Arial,sans-serif;font-size:14px;line-height:140%;padding:25px 35px;padding-top:0;text-align:center'>";
        content += "<div style='font-weight:bold;padding-bottom:15px'> 인증 번호 </div>";
        content += "<div style='color:#000;font-size:36px;font-weight:bold;padding-bottom:15px'>" + authNum + "</div>";
        content += "<div>(이 코드는 5분 동안 유효합니다.)</div>";
        content += "</td>";
        content += "</tr>";
        content += "</div>";

        message.setText(content, "utf-8", "html");
        return message;
    }

    @Override
    public void verifyAuthNumber(String authNumber,String email) {
        final String emailValue= redisUtil.getData(authNumber);
        if(emailValue==null||emailValue.equals("")){
            throw new CustomException(AUTH_NUMBER_MISS_MATCH);
        }
        if(!emailValue.equals(email)){
            throw new CustomException(AUTH_NUMBER_MISS_MATCH);
        }
        redisUtil.deleteData(authNumber);
    }
}
