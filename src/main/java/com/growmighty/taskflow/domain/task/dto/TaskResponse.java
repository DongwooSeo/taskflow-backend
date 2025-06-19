package com.growmighty.taskflow.domain.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.growmighty.taskflow.domain.auth.dto.UserResponse;
import com.growmighty.taskflow.domain.task.entity.Priority;
import com.growmighty.taskflow.domain.task.entity.Status;
import com.growmighty.taskflow.domain.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final Priority priority;
    private final Status status;
    
    private final UserResponse assignee;
    private final UserResponse creator;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime dueDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime startedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assignee(task.getAssignee() != null ? UserResponse.from(task.getAssignee()) : null)
                .creator(UserResponse.from(task.getCreator()))
                .dueDate(task.getDueDate())
                .startedAt(task.getStartedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
} 