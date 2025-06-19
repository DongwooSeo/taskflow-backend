package com.growmighty.taskflow.domain.activity.aspect;

import com.growmighty.taskflow.domain.activity.ActivityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {
    ActivityType type();
    String targetIdExpression() default "#result.id"; // 기본값으로 반환값의 id를 사용
} 