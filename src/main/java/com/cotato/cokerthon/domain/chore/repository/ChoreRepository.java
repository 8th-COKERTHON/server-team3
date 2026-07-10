package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.Chore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoreRepository extends JpaRepository<Chore, Long> {
}