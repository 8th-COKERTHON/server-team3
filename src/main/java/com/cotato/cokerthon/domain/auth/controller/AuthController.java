package com.cotato.cokerthon.domain.auth.controller;

import com.cotato.cokerthon.domain.auth.dto.request.LoginRequest;
import com.cotato.cokerthon.domain.auth.dto.request.SignupRequest;
import com.cotato.cokerthon.domain.auth.dto.response.AuthResponse;
import com.cotato.cokerthon.domain.auth.service.AuthService;
import com.cotato.cokerthon.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입 API", description = "닉네임, 아이디, 비밀번호를 입력받아 회원가입합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 닉네임 또는 아이디")
    })
    @PostMapping("/signup")
    public CommonResponse<AuthResponse> signUp(@Valid @RequestBody SignupRequest request) {

        AuthResponse response = authService.signup(request);
        return CommonResponse.success(response);
    }

    @Operation(summary = "로그인 API", description = "아이디와 비밀번호로 로그인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호 불일치")
    })
    @PostMapping("/login")
    public CommonResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        AuthResponse response = authService.login(request);

        // 세션 생성
        HttpSession session = httpRequest.getSession(true);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        return CommonResponse.success(response);
    }

    @Operation(summary = "로그아웃 API", description = "현재 사용자의 세션을 만료시키고 쿠키를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping("/logout")
    public CommonResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보 있으면 로그아웃 처리
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return CommonResponse.success(null);
    }

}