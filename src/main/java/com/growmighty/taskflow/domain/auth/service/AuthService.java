package com.growmighty.taskflow.domain.auth.service;

import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.common.filter.JwtTokenProvider;
import com.growmighty.taskflow.common.util.PasswordEncoder;
import com.growmighty.taskflow.domain.auth.dto.AuthResponse;
import com.growmighty.taskflow.domain.auth.dto.SignInRequest;
import com.growmighty.taskflow.domain.auth.dto.SignUpRequest;
import com.growmighty.taskflow.domain.auth.dto.WithdrawRequest;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        
        User user = User.createUser(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName()
        );
        
        userRepository.save(user);
        log.debug("새로운 사용자 등록: {}", user.getUsername());
        
        return AuthResponse.fromUser(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(SignInRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("로그인 실패: 존재하지 않는 사용자명 - {}", request.getUsername());
                    return new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
                });
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("로그인 실패: 잘못된 비밀번호 - {}", request.getUsername());
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED);
        }
        
        String token = tokenProvider.generateToken(user.getId());
        log.debug("로그인 성공: {}", user.getUsername());
        
        return AuthResponse.withToken(token);
    }

    @Transactional
    public void withdraw(WithdrawRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        user.delete();
        log.debug("회원 탈퇴 완료: {}", user.getUsername());
    }
} 