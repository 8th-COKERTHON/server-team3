package com.cotato.cokerthon.domain.member.entity;

import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 다른 팀원이 구현 예정 — 현재는 컴파일용 스텁
@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    // TODO: 다른 팀원이 필드 추가 예정
    private int totalPoints;
}
