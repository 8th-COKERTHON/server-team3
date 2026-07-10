package com.cotato.cokerthon.domain.chore.dto.response;

import com.cotato.cokerthon.domain.chore.entity.Chore;
import com.cotato.cokerthon.domain.chore.entity.Difficulty;

public record ChoreResponse(
        Long id,
        String category,
        String name,
        Difficulty difficulty,
        int score,
        String memo
) {
    public static ChoreResponse from(Chore chore) {
        return new ChoreResponse(
                chore.getId(),
                chore.getCategory(),
                chore.getName(),
                chore.getDifficulty(),
                chore.getScore(),
                chore.getMemo()
        );
    }
}