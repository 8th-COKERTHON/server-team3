package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChoreRepository extends JpaRepository<GroupChore, Long> {

    List<GroupChore> findByGroup_IdOrderByDateAsc(Long groupId);
}
