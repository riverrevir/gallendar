package com.gallendar.gradle.server.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_INPUT_VALUE(BAD_REQUEST, "잘못된 요청입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),
    AUTH_NUMBER_MISS_MATCH(UNAUTHORIZED,"인증번호를 잘못 입력하였습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    EMAIL_NOT_FOUND(NOT_FOUND,"해당 이메일로 가입된 유저 정보를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR,"너무 많은 사용자로 인해 서버 상태가 좋지 않습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String detail;
}
