package com.cotato.cokerthon.domain.roulette.dto.response;

import com.cotato.cokerthon.domain.roulette.entity.RouletteResult;

import java.time.LocalDate;

public record RouletteResultResponse(
        Long id,
        Long winnerId,
        Long choreId,
        LocalDate nextWeekStartDate
) {
    public static RouletteResultResponse from(RouletteResult result) {
        return new RouletteResultResponse(
                result.getId(),
                result.getMember().getId(),
                result.getChore().getId(),
                result.getNextWeekStartDate()
        );
    }
}
