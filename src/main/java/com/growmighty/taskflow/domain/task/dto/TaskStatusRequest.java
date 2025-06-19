package com.growmighty.taskflow.domain.task.dto;

import com.growmighty.taskflow.domain.task.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskStatusRequest {
    
    @NotNull(message = "상태값은 필수 입력값입니다.")
    private Status status;
} 