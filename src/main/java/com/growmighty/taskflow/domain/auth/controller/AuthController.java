package com.growmighty.taskflow.domain.auth.controller;

import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.domain.auth.dto.AuthResponse;
import com.growmighty.taskflow.domain.auth.dto.SignInRequest;
import com.growmighty.taskflow.domain.auth.dto.SignUpRequest;
import com.growmighty.taskflow.domain.auth.dto.WithdrawRequest;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody SignUpRequest request
    ) {
        log.debug("회원가입 요청: username={}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody SignInRequest request
    ) {
        log.debug("로그인 요청: username={}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", response));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal User user
    ) {
        authService.withdraw(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("회원탈퇴가 완료되었습니다.", null));
    }
} 