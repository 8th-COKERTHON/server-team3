package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.ChoreStatus;
import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GroupChoreRepository extends JpaRepository<GroupChore, Long> {

    List<GroupChore> findByGroup_IdOrderByDateAsc(Long groupId);

    List<GroupChore> findByGroup_IdAndStatusAndDateBetween(Long groupId, ChoreStatus status, LocalDate startDate, LocalDate endDate);
}
