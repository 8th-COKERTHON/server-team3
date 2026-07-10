package com.cotato.cokerthon.domain.chore.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChoreErrorCode implements ErrorCode {

    CHORE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "CHORE_ERROR_404_NOT_FOUND", "요청을 찾을 수 없습니다."),
    UNAUTHORIZED_COMPLETE(HttpStatus.FORBIDDEN, "CHORE_ERROR_403_FORBIDDEN", "요청을 완료할 권한이 없습니다."),
    ALREADY_COMPLETED(HttpStatus.CONFLICT, "CHORE_ERROR_409_CONFLICT", "이미 완료된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
