package com.cotato.cokerthon.domain.auth.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "AUTH_ERROR_409_CONFLICT", "중복되는 아이디가 존재합니다."),

    MEMBER_NOT_FOUND(HttpStatus.CONFLICT, "AUTH_ERROR_404_NOT_FOUND", "사용자를 찾을 수 없습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_ERROR_401_UNAUTHORIZED", "아이디 또는 비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}