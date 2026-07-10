package com.cotato.cokerthon.domain.auth.dto.response;

import com.cotato.cokerthon.domain.member.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record AuthResponse(
        Long id,
        String nickname,
        String loginId,
        List<Long> groupIds
){
    public static AuthResponse from(Member member, List<Long> groupIds) {
        return new AuthResponse(
                member.getId(),
                member.getName(),
                member.getLoginId(),
                groupIds
        );
    }
}