package com.growmighty.taskflow.common.aspect.logging;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class LogInfo {
    private String methodName;          // 메서드 이름
    private String className;           // 클래스 이름
    private String userName;            // 사용자 이름
    private Object[] parameters;        // 메서드 파라미터
    private Object result;              // 메서드 실행 결과
    private long executionTime;         // 실행 시간 (ms)
    private LocalDateTime timestamp;    // 로그 생성 시간
    private String exceptionMessage;    // 예외 메시지 (있는 경우)
} 