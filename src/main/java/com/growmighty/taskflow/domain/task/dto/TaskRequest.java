package com.growmighty.taskflow.domain.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growmighty.taskflow.domain.task.entity.Priority;
import com.growmighty.taskflow.domain.task.entity.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TaskRequest {

    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;

    private String description;

    @NotNull(message = "우선순위는 필수 입력값입니다.")
    private Priority priority;

    private Long assigneeId;

    @Future(message = "마감일은 현재 시간 이후여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    private Status status;
} 