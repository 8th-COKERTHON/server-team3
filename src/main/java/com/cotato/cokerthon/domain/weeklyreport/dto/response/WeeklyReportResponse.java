package com.cotato.cokerthon.domain.weeklyreport.dto.response;

import com.cotato.cokerthon.domain.weeklyreport.entity.WeeklyReport;

import java.time.LocalDate;

public record WeeklyReportResponse(
        Long id,
        Long memberId,
        LocalDate weekStartDate,
        int totalPoints,
        double shareRatio,
        Integer rank
) {
    public static WeeklyReportResponse from(WeeklyReport report) {
        return new WeeklyReportResponse(
                report.getId(),
                report.getMember().getId(),
                report.getWeekStartDate(),
                report.getTotalPoints(),
                report.getShareRatio(),
                report.getRank()
        );
    }
}
