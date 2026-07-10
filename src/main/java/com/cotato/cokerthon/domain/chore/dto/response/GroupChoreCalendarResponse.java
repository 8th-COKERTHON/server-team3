package com.cotato.cokerthon.domain.chore.dto.response;

import java.time.LocalDate;
import java.util.List;

public record GroupChoreCalendarResponse(
        LocalDate date,
        List<GroupChoreResponse> chores
) {
}