package com.cotato.cokerthon.domain.weeklyreport.repository;

import com.cotato.cokerthon.domain.weeklyreport.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {

    List<WeeklyReport> findByWeekStartDate(LocalDate weekStartDate);

    Optional<WeeklyReport> findByMemberIdAndWeekStartDate(Long memberId, LocalDate weekStartDate);

    boolean existsByMemberIdAndWeekStartDate(Long memberId, LocalDate weekStartDate);
}
