package com.cotato.cokerthon.domain.roulette.exception;

import com.cotato.cokerthon.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RouletteErrorCode implements ErrorCode {

    ROULETTE_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "ROULETTE_ERROR_404_NOT_FOUND", "해당 주차의 룰렛 결과를 찾을 수 없습니다."),
    ROULETTE_ALREADY_SPUN(HttpStatus.CONFLICT, "ROULETTE_ERROR_409_CONFLICT", "해당 집안일은 이미 룰렛이 완료되었습니다."),
    NO_MEMBERS(HttpStatus.BAD_REQUEST, "ROULETTE_ERROR_400_NO_MEMBERS", "룰렛을 돌릴 멤버가 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
