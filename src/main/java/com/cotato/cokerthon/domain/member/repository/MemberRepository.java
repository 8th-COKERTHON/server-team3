package com.cotato.cokerthon.domain.member.repository;

import com.cotato.cokerthon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}
