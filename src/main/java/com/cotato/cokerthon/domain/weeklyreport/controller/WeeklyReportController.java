package com.cotato.cokerthon.domain.weeklyreport.controller;

import com.cotato.cokerthon.domain.weeklyreport.dto.response.WeeklyReportResponse;
import com.cotato.cokerthon.domain.weeklyreport.service.WeeklyReportService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "WeeklyReport", description = "주간 리포트 API")
@RestController
@RequestMapping("/api/weekly-reports")
@RequiredArgsConstructor
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    @Operation(summary = "주간 리포트 조회", description = "해당 주차 전체 멤버의 기여도 리포트를 반환합니다.")
    @GetMapping
    public CommonResponse<List<WeeklyReportResponse>> getWeeklyReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate
    ) {
        return CommonResponse.success(weeklyReportService.getWeeklyReports(weekStartDate));
    }

    @Operation(summary = "주간 리포트 집계", description = "해당 주차의 shareRatio와 rank를 계산합니다.")
    @PostMapping("/calculate")
    public CommonResponse<Void> calculateShareRatio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate
    ) {
        weeklyReportService.calculateShareRatioAndRank(weekStartDate);
        return CommonResponse.success(null);
    }
}
