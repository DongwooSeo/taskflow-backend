package com.growmighty.taskflow.domain.task.controller;

import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.domain.task.dto.TaskRequest;
import com.growmighty.taskflow.domain.task.dto.TaskResponse;
import com.growmighty.taskflow.domain.task.dto.TaskStatusRequest;
import com.growmighty.taskflow.domain.task.entity.Status;
import com.growmighty.taskflow.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest request,
            @RequestAttribute Long userId
    ) {
        log.debug("Task 생성 요청: title={}, assigneeId={}", request.getTitle(), request.getAssigneeId());
        TaskResponse response = taskService.createTask(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task가 생성되었습니다.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponse>>> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String search,
            Pageable pageable,
            @RequestAttribute Long userId
    ) {
        log.debug("Task 목록 조회 요청: status={}, assigneeId={}, search={}", status, assigneeId, search);
        Page<TaskResponse> response = taskService.getTasks(status, assigneeId, search, pageable);
        return ResponseEntity.ok(ApiResponse.success("Task 목록을 조회했습니다.", response));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTask(
            @PathVariable Long taskId,
            @RequestAttribute Long userId
    ) {
        log.debug("Task 상세 조회 요청: taskId={}", taskId);
        TaskResponse response = taskService.getTask(taskId);
        return ResponseEntity.ok(ApiResponse.success("Task를 조회했습니다.", response));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest request,
            @RequestAttribute Long userId
    ) {
        log.debug("Task 수정 요청: taskId={}, title={}", taskId, request.getTitle());
        TaskResponse response = taskService.updateTask(taskId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Task가 수정되었습니다.", response));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long taskId,
            @RequestAttribute Long userId
    ) {
        log.debug("Task 삭제 요청: taskId={}", taskId);
        taskService.deleteTask(taskId, userId);
        return ResponseEntity.ok(ApiResponse.success("Task가 삭제되었습니다.", null));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskStatusRequest request,
            @RequestAttribute Long userId) {
        
        TaskResponse updatedTask = taskService.updateTaskStatus(taskId, request);
        
        return ResponseEntity.ok(ApiResponse.success("작업 상태가 업데이트되었습니다.", updatedTask));
    }
} 