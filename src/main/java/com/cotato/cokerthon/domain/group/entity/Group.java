package com.cotato.cokerthon.domain.group.entity;

import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "group_info")
@Getter
@NoArgsConstructor
public class Group extends BaseTimeEntity {

    private String name;

    @Column(name = "invite_code", unique = true, nullable = false)
    private String inviteCode;


    public Group(String name) {
        this.name = name;
        this.inviteCode = generateInviteCode();
    }

    // 8자리의 고유한 초대 코드 생성
    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}