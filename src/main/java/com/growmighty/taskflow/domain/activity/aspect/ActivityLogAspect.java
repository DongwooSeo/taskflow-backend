package com.growmighty.taskflow.domain.activity.aspect;

import com.growmighty.taskflow.domain.activity.ActivityLog;
import com.growmighty.taskflow.domain.activity.ActivityLogRepository;
import com.growmighty.taskflow.domain.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogRepository activityLogRepository;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Around("@annotation(logActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {
        // 먼저 메서드를 실행하고 결과를 받음
        Object result = joinPoint.proceed();

        try {
            // 요청 정보 가져오기
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            // 인증 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = -1L;

            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof User user) {
                    userId = user.getId();
                } else {
                    log.warn("Principal is not of type User. Actual type: {}", principal.getClass().getName());
                }
            }

            // targetId 파싱 시도
            Long targetId = null;
            try {
                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setVariable("result", result);
                context.setVariable("args", joinPoint.getArgs());
                targetId = expressionParser.parseExpression(logActivity.targetIdExpression())
                        .getValue(context, Long.class);
            } catch (Exception e) {
                log.warn("Failed to parse targetId from expression: {}, Error: {}",
                        logActivity.targetIdExpression(), e.getMessage());
                // targetId 파싱 실패 시 null로 처리하고 계속 진행
            }

            // 활동 로그 생성 및 저장
            ActivityLog activityLog = ActivityLog.builder()
                    .userId(userId)
                    .ipAddress(request.getRemoteAddr())
                    .requestMethod(request.getMethod())
                    .requestUrl(request.getRequestURI())
                    .activityType(logActivity.type())
                    .targetId(targetId) // null이 될 수 있음
                    .build();

            activityLogRepository.save(activityLog);
        } catch (Exception e) {
            // 로깅 실패 시 경고 로그만 남기고 원래 로직은 계속 진행
            log.error("Failed to log activity: {}", e.getMessage());
        }

        return result;
    }
} 