package com.cotato.cokerthon.domain.group.dto.response;

import java.util.List;

public record GroupChoreReportResponse(
        String targetWeek,
        List<MemberChoreRankInfo> rankings
) {
    public record MemberChoreRankInfo(
            int rank,
            Long memberId,
            String memberName,
            int score,
            double percentage
    ) {
    }
}