package com.cotato.cokerthon.domain.auth.service;

import com.cotato.cokerthon.domain.auth.dto.request.LoginRequest;
import com.cotato.cokerthon.domain.auth.dto.request.SignupRequest;
import com.cotato.cokerthon.domain.auth.dto.response.AuthResponse;
import com.cotato.cokerthon.domain.auth.exception.AuthErrorCode;
import com.cotato.cokerthon.domain.member.entity.Member;
import com.cotato.cokerthon.domain.member.repository.MemberRepository;
import com.cotato.cokerthon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse signup(SignupRequest request) {

        // 아이디 중복 확인
        if (memberRepository.findByLoginId(request.loginId()).isPresent()) {
            throw new CustomException(AuthErrorCode.DUPLICATE_LOGIN_ID);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        // 4. User 엔티티 생성 및 저장
        Member member = Member.builder()
                .loginId(request.loginId())
                .password(encodedPassword)
                .name(request.name())
                .build();

        Member newMember = memberRepository.save(member);
        return AuthResponse.from(newMember);
    }

    public AuthResponse login(LoginRequest request) {

        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new CustomException(AuthErrorCode.MEMBER_NOT_FOUND));

        // 시큐리티에게 로그인 검사 요청
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.loginId(),
                request.password()
        );

        try {
            // 비밀번호 확인
            Authentication authentication = authenticationManager.authenticate(token);

            // 세션 확인
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return AuthResponse.from(member);
        } catch (BadCredentialsException e) {
            throw new CustomException(AuthErrorCode.UNAUTHORIZED);
        }


    }

}
