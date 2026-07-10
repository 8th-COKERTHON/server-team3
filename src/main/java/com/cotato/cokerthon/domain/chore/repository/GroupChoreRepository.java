package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.GroupChore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChoreRepository extends JpaRepository<GroupChore, Long> {
}
