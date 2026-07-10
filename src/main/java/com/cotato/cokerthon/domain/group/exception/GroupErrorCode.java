package com.cotato.cokerthon.domain.group.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCode {

    // 404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_ERROR_404_MEMBER_NOT_FOUND", "존재하지 않는 회원입니다."),
    INVALID_INVITE_CODE(HttpStatus.NOT_FOUND, "GROUP_ERROR_404_INVALID_INVITE_CODE", "올바르지 않은 초대 코드입니다."),

    // 409 Conflict
    ALREADY_JOINED_GROUP(HttpStatus.CONFLICT, "GROUP_ERROR_409_ALREADY_JOINED", "이미 가입된 그룹입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
