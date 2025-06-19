package com.growmighty.taskflow.domain.task.service;

import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.service.UserService;
import com.growmighty.taskflow.domain.task.dto.TaskRequest;
import com.growmighty.taskflow.domain.task.dto.TaskResponse;
import com.growmighty.taskflow.domain.task.dto.TaskStatusRequest;
import com.growmighty.taskflow.domain.task.entity.Status;
import com.growmighty.taskflow.domain.task.entity.Task;
import com.growmighty.taskflow.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Task getTaskById(Long taskId) {
        return taskRepository.findByIdAndDeletedFalse(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TASK_NOT_FOUND));
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request, Long currentUserId) {
        User creator = userService.getUserById(currentUserId);

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userService.getUserById(request.getAssigneeId());
        }

        Task task = Task.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                assignee,
                creator,
                request.getDueDate()
        );

        taskRepository.save(task);
        log.debug("새로운 Task 생성: {}", task.getTitle());

        return TaskResponse.from(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(Status status, Long assigneeId, String search, Pageable pageable) {
        return taskRepository.findAllByFilters(status, assigneeId, search, pageable)
                .map(TaskResponse::from);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(Long taskId) {
        Task task = getTaskById(taskId);
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request, Long currentUserId) {
        Task task = getTaskById(taskId);

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userService.getUserById(request.getAssigneeId());
        }

        task.update(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                assignee,
                request.getDueDate()
        );

        if (request.getStatus() != null && request.getStatus() != task.getStatus()) {
            task.updateStatus(request.getStatus());
        }

        log.debug("Task 수정: {}", task.getTitle());
        return TaskResponse.from(task);
    }

    @Transactional
    public void deleteTask(Long taskId, Long currentUserId) {
        Task task = getTaskById(taskId);
        task.delete();
        log.debug("Task 삭제: {}", task.getTitle());
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, TaskStatusRequest request) {
        Task task = getTaskById(taskId);
        task.updateStatus(request.getStatus());
        log.debug("Task 상태 업데이트: {} -> {}", task.getTitle(), request.getStatus());
        
        return TaskResponse.from(task);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long taskId) {
        return taskRepository.existsById(taskId);
    }
} 