package com.growmighty.taskflow.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Authentication & Authorization
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "잘못된 사용자명 또는 비밀번호입니다"),
    
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "올바르지 않은 이메일 형식입니다"),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "이름은 2자 이상이어야 합니다"),
    
    // Request Validation
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다"),
    MISSING_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 타입입니다"),
    
    // Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP Method입니다");

    private final HttpStatus status;
    private final String message;
} 