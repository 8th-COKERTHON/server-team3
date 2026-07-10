package com.cotato.cokerthon.domain.group.repository;

import com.cotato.cokerthon.domain.group.entity.Group;
import com.cotato.cokerthon.domain.group.entity.GroupMember;
import com.cotato.cokerthon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByGroupAndMember(Group group, Member member);
}
