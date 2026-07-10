package com.cotato.cokerthon.global.security;

import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) {

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(com.cotato.cokerthon.domain.auth.exception.AuthErrorCode.MEMBER_NOT_FOUND));

        return new CustomUserDetails(member);
    }

}
