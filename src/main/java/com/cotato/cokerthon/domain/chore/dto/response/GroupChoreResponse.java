package com.cotato.cokerthon.domain.chore.dto.response;

import com.cotato.cokerthon.domain.chore.entity.AssignType;
import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import com.cotato.cokerthon.domain.chore.entity.Difficulty;
import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import com.cotato.cokerthon.domain.chore.entity.RepeatCycle;

import java.time.LocalDate;

public record GroupChoreResponse(
        Long id,
        Long groupId,
        Long choreId,
        String category,
        String name,
        LocalDate date,
        Difficulty difficulty,
        AssignType assignType,
        Long assigneeId,
        String assigneeName,
        RepeatCycle repeatCycle,
        String repeatPattern,
        String memo,
        int score,
        ChoreStatus status
) {
    public static GroupChoreResponse from(GroupChore groupChore) {
        return new GroupChoreResponse(
                groupChore.getId(),
                groupChore.getGroup().getId(),
                groupChore.getChore() != null ? groupChore.getChore().getId() : null,
                groupChore.getChore() != null ? groupChore.getChore().getCategory() : null,
                groupChore.getName(),
                groupChore.getDate(),
                groupChore.getEffectiveDifficulty(),
                groupChore.getAssignType(),
                groupChore.getAssignee() != null ? groupChore.getAssignee().getId() : null,
                groupChore.getAssignee() != null ? groupChore.getAssignee().getName() : null,
                groupChore.getRepeatCycle(),
                groupChore.getRepeatPattern(),
                groupChore.getMemo(),
                groupChore.getEffectiveScore(),
                groupChore.getStatus()
        );
    }
}