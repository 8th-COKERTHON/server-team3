package com.cotato.cokerthon.domain.auth.dto.response;

import com.cotato.cokerthon.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String nickname,
        String loginId
){
    public static AuthResponse from(Member member) {
        return new AuthResponse(
                member.getId(),
                member.getName(),
                member.getLoginId()
        );
    }
}
