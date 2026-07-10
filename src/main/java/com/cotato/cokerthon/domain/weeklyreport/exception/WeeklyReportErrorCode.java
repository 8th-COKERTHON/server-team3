package com.cotato.cokerthon.domain.weeklyreport.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WeeklyReportErrorCode implements ErrorCode {

    WEEKLY_REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "WEEKLY_REPORT_ERROR_404_NOT_FOUND", "해당 주차의 리포트를 찾을 수 없습니다."),
    WEEKLY_REPORT_ALREADY_EXISTS(HttpStatus.CONFLICT, "WEEKLY_REPORT_ERROR_409_CONFLICT", "이미 해당 주차의 리포트가 존재합니다."),
    NO_REPORTS_FOR_WEEK(HttpStatus.BAD_REQUEST, "WEEKLY_REPORT_ERROR_400_EMPTY", "해당 주차에 집계된 리포트가 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
