package com.cotato.cokerthon.domain.chore.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GroupChoreDailyResponse(
        LocalDate date,
        int totalCount,
        int completedCount,
        List<GroupChoreResponse> chores
) {
}