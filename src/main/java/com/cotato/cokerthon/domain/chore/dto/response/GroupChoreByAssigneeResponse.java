package com.cotato.cokerthon.domain.chore.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GroupChoreByAssigneeResponse(
        LocalDate date,
        Long memberId,
        String memberName,
        int totalCount,
        int completedCount,
        List<GroupChoreResponse> chores
) {
}