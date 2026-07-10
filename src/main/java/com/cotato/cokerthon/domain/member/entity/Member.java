package com.cotato.cokerthon.domain.member.entity;

import com.cotato.cokerthon.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    /** 로그인 ID */
    @Column(name = "login_id", nullable = false, length = 256, unique = true)
    private String loginId;

    /** 비밀번호 */
    @Column(nullable = false, length = 256)
    private String password;

    /** 이름 */
    @Column(nullable = false, length = 256, unique = true)
    private String name;

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}