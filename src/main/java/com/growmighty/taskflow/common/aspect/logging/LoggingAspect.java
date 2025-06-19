package com.growmighty.taskflow.common.aspect.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    public LoggingAspect() {
        this.objectMapper = createObjectMapper();
    }

    @Around("@annotation(Loggable)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        LogInfo.LogInfoBuilder logBuilder = buildBaseLogInfo(joinPoint);
        Object result = null;

        try {
            result = joinPoint.proceed();
            logBuilder.result(result);
            return result;
        } catch (Throwable ex) {
            logBuilder.exceptionMessage(ex.getMessage());
            throw ex;
        } finally {
            logBuilder.executionTime(System.currentTimeMillis() - start);
            log.info(buildLogMessage(logBuilder.build(), getLoggableValue(joinPoint)));
        }
    }

    private String getLoggableValue(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Loggable loggable = method.getAnnotation(Loggable.class);
        return loggable != null && !loggable.value().isEmpty() ? loggable.value() : "Method Execution";
    }

    private LogInfo.LogInfoBuilder buildBaseLogInfo(ProceedingJoinPoint joinPoint) {
        return LogInfo.builder()
                .timestamp(LocalDateTime.now())
                .className(joinPoint.getTarget().getClass().getSimpleName())
                .methodName(joinPoint.getSignature().getName())
                .parameters(joinPoint.getArgs())
                .userName(getCurrentUsername());
    }

    private String buildLogMessage(LogInfo logInfo, String logTitle) {
        try {
            StringBuilder sb = new StringBuilder()
                .append("\n=== ").append(logTitle).append(" ===\n")
                .append("Class: ").append(logInfo.getClassName()).append("\n")
                .append("Method: ").append(logInfo.getMethodName()).append("\n")
                .append("User: ").append(logInfo.getUserName()).append("\n")
                .append("Timestamp: ").append(logInfo.getTimestamp()).append("\n")
                .append("Parameters:\n").append(objectToPrettyJson(logInfo.getParameters()))
                .append("\nExecution Time: ").append(logInfo.getExecutionTime()).append("ms\n");

            if (logInfo.getExceptionMessage() != null) {
                sb.append("Exception: ").append(logInfo.getExceptionMessage()).append("\n");
            } else {
                sb.append("Result:\n").append(objectToPrettyJson(logInfo.getResult()));
            }

            sb.append("\n").append("=".repeat(logTitle.length() + 8));
            return sb.toString();
        } catch (Exception e) {
            log.error("Error while serializing log info", e);
            return "Error while logging method execution.";
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.findAndRegisterModules();
        return mapper;
    }

    private String objectToPrettyJson(Object obj) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : "ANONYMOUS";
    }
}
