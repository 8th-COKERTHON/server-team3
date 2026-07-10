package com.cotato.cokerthon.domain.member.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    // 404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_ERROR_404_NOT_FOUND", "존재하지 않는 회원입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}