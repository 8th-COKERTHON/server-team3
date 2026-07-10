package com.cotato.cokerthon.domain.chore.repository;

import com.cotato.cokerthon.domain.chore.entity.ChoreRequest;
import com.cotato.cokerthon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoreRequestRepository extends JpaRepository<ChoreRequest, Long> {

    List<ChoreRequest> findByReceiverOrderByCreatedAtDesc(Member receiver);

    List<ChoreRequest> findBySenderOrderByCreatedAtDesc(Member sender);

    // 받는 사람 기준 안 읽은 요청 수
    long countByReceiverAndIsReadFalse(Member receiver);

    // 보낸 사람 기준 완료됐는데 아직 안 읽은 수
    long countBySenderAndStatusAndCompletionReadFalse(Member sender, com.cotato.cokerthon.domain.chore.entity.ChoreStatus status);
}
