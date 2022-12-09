package com.gallendar.gradle.server.members.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class EmailRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    public String email;
}
