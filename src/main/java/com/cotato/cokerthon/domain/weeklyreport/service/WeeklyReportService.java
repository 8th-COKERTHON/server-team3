package com.cotato.cokerthon.domain.weeklyreport.service;

import com.cotato.cokerthon.domain.weeklyreport.dto.response.WeeklyReportResponse;
import com.cotato.cokerthon.domain.weeklyreport.entity.WeeklyReport;
import com.cotato.cokerthon.domain.weeklyreport.exception.WeeklyReportErrorCode;
import com.cotato.cokerthon.domain.weeklyreport.repository.WeeklyReportRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;

    // 특정 주차 리포트 전체 조회
    public List<WeeklyReportResponse> getWeeklyReports(LocalDate weekStartDate) {
        List<WeeklyReport> reports = weeklyReportRepository.findByWeekStartDate(weekStartDate);
        if (reports.isEmpty()) {
            throw new CustomException(WeeklyReportErrorCode.NO_REPORTS_FOR_WEEK);
        }
        return reports.stream()
                .map(WeeklyReportResponse::from)
                .toList();
    }

    // 주간 집계 완료 후 shareRatio & rank 계산하여 저장
    @Transactional
    public void calculateShareRatioAndRank(LocalDate weekStartDate) {
        List<WeeklyReport> reports = weeklyReportRepository.findByWeekStartDate(weekStartDate);
        if (reports.isEmpty()) {
            throw new CustomException(WeeklyReportErrorCode.NO_REPORTS_FOR_WEEK);
        }

        int memberCount = reports.size();
        double totalPoints = reports.stream().mapToInt(WeeklyReport::getTotalPoints).sum();
        double fairShare = totalPoints / memberCount;

        // 각자의 부족분(공평 기준 - 자신의 포인트, 0 미만은 0으로 클램프)
        double[] deficiencies = reports.stream()
                .mapToDouble(r -> Math.max(0.0, fairShare - r.getTotalPoints()))
                .toArray();

        double totalDeficiency = 0;
        for (double d : deficiencies) totalDeficiency += d;

        // rank: totalPoints 내림차순 (1등 = 가장 많이 일한 사람)
        List<WeeklyReport> ranked = reports.stream()
                .sorted(Comparator.comparingInt(WeeklyReport::getTotalPoints).reversed())
                .toList();

        for (int i = 0; i < reports.size(); i++) {
            WeeklyReport report = reports.get(i);
            double deficiency = deficiencies[i];

            // 모두가 동등하게 기여했으면 균등 배분, 아니면 부족 비율로 계산
            double ratio = (totalDeficiency == 0)
                    ? (100.0 / memberCount)
                    : (deficiency / totalDeficiency * 100.0);

            int rank = ranked.indexOf(report) + 1;
            report.updateShareRatioAndRank(ratio, rank);
        }
    }
}
