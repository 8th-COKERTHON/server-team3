package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.AssignType;
import com.cotato.cokerthon.domain.chore.entity.Chore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// TODO: 다른 팀원이 구현 예정 — 현재는 컴파일용 스텁
public interface ChoreRepository extends JpaRepository<Chore, Long> {

    List<Chore> findByAssignType(AssignType assignType);
}
