package com.cotato.cokerthon.domain.chore.dto.response;

import com.cotato.cokerthon.domain.chore.entity.AssignType;
import com.cotato.cokerthon.domain.chore.entity.Difficulty;
import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import com.cotato.cokerthon.domain.chore.entity.RepeatCycle;

import java.time.LocalDate;

public record GroupChoreResponse(
        Long id,
        Long groupId,
        String name,
        LocalDate date,
        Difficulty difficulty,
        AssignType assignType,
        Long assigneeId,
        String assigneeName,
        RepeatCycle repeatCycle,
        String repeatPattern,
        String memo,
        int score
) {
    public static GroupChoreResponse from(GroupChore chore) {
        return new GroupChoreResponse(
                chore.getId(),
                chore.getGroup().getId(),
                chore.getName(),
                chore.getDate(),
                chore.getDifficulty(),
                chore.getAssignType(),
                chore.getAssignee() != null ? chore.getAssignee().getId() : null,
                chore.getAssignee() != null ? chore.getAssignee().getName() : null,
                chore.getRepeatCycle(),
                chore.getRepeatPattern(),
                chore.getMemo(),
                chore.getScore()
        );
    }
}
