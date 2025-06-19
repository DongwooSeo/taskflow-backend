package com.growmighty.taskflow.common.aspect.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    String value() default "";  // 추가적인 로깅 설명을 위한 옵션 값
} 