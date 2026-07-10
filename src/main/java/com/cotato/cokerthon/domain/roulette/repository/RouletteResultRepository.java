package com.cotato.cokerthon.domain.roulette.repository;

import com.cotato.cokerthon.domain.roulette.entity.RouletteResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RouletteResultRepository extends JpaRepository<RouletteResult, Long> {

    List<RouletteResult> findByNextWeekStartDate(LocalDate nextWeekStartDate);

    boolean existsByChoreIdAndNextWeekStartDate(Long choreId, LocalDate nextWeekStartDate);
}
