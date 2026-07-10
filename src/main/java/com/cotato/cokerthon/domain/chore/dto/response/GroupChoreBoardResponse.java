package com.cotato.cokerthon.domain.chore.dto.response;

import java.util.List;

public record GroupChoreBoardResponse(
        List<GroupChoreResponse> scheduled,  // 예정
        List<GroupChoreResponse> inProgress, // 진행중
        List<GroupChoreResponse> done        // 완료
) {
}