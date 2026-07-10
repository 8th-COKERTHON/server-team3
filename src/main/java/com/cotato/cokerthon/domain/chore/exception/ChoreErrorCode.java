package com.cotato.cokerthon.domain.chore.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChoreErrorCode implements ErrorCode {

    // 404 Not Found
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "CHORE_ERROR_404_GROUP_NOT_FOUND", "존재하지 않는 그룹입니다."),
    ASSIGNEE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHORE_ERROR_404_ASSIGNEE_NOT_FOUND", "존재하지 않는 담당자입니다."),
    CHORE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHORE_ERROR_404_CHORE_NOT_FOUND", "존재하지 않는 집안일입니다."),
    CHORE_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHORE_ERROR_404_CHORE_ITEM_NOT_FOUND", "존재하지 않는 집안일 항목입니다."),

    // 400 Bad Request
    ASSIGNEE_NOT_IN_GROUP(HttpStatus.BAD_REQUEST, "CHORE_ERROR_400_ASSIGNEE_NOT_IN_GROUP", "담당자가 해당 그룹에 속해있지 않습니다."),
    ASSIGNEE_REQUIRED(HttpStatus.BAD_REQUEST, "CHORE_ERROR_400_ASSIGNEE_REQUIRED", "직접선택 시 담당자를 지정해야 합니다."),
    REPEAT_PATTERN_REQUIRED(HttpStatus.BAD_REQUEST, "CHORE_ERROR_400_REPEAT_PATTERN_REQUIRED", "반복 주기에 맞는 세부 패턴을 선택해야 합니다."),
    INVALID_REPEAT_PATTERN(HttpStatus.BAD_REQUEST, "CHORE_ERROR_400_INVALID_REPEAT_PATTERN", "반복 세부 패턴 형식이 올바르지 않습니다."),
    CHORE_NOT_IN_GROUP(HttpStatus.BAD_REQUEST, "CHORE_ERROR_400_CHORE_NOT_IN_GROUP", "해당 집안일이 그룹에 속해있지 않습니다."),
    CHORE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "CHORE_ERROR_404_NOT_FOUND", "요청을 찾을 수 없습니다."),
    UNAUTHORIZED_DONE(HttpStatus.FORBIDDEN, "CHORE_ERROR_403_FORBIDDEN", "요청을 완료할 권한이 없습니다."),
    ALREADY_DONE(HttpStatus.CONFLICT, "CHORE_ERROR_409_CONFLICT", "이미 완료된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
