package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.AssignType;
import com.cotato.cokerthon.domain.chore.entity.Chore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoreRepository extends JpaRepository<Chore, Long> {

    List<Chore> findByAssignType(AssignType assignType);
}
