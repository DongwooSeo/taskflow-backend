package com.growmighty.taskflow.domain.comment.service;

import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.service.UserService;
import com.growmighty.taskflow.domain.comment.dto.CommentRequest;
import com.growmighty.taskflow.domain.comment.dto.CommentResponse;
import com.growmighty.taskflow.domain.comment.entity.Comment;
import com.growmighty.taskflow.domain.comment.repository.CommentRepository;
import com.growmighty.taskflow.domain.task.entity.Task;
import com.growmighty.taskflow.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public CommentResponse createComment(Long taskId, CommentRequest request, Long currentUserId) {
        Task task = taskService.getTaskById(taskId);
        User user = userService.getUserById(currentUserId);

        Comment comment = Comment.createComment(request.getContent(), task, user);
        commentRepository.save(comment);
        
        log.debug("새로운 댓글 생성: taskId={}, userId={}", taskId, currentUserId);
        return CommentResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long taskId, Pageable pageable) {
        if (!taskService.existsById(taskId)) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        return commentRepository.findAllByTaskId(taskId, pageable)
                .map(CommentResponse::from);
    }

    @Transactional
    public CommentResponse updateComment(Long taskId, Long commentId, CommentRequest request, Long currentUserId) {
        Comment comment = getCommentById(commentId);

        if (!comment.getTask().getId().equals(taskId)) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        comment.update(request.getContent());
        log.debug("댓글 수정: commentId={}, taskId={}", commentId, taskId);
        
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long taskId, Long commentId, Long currentUserId) {
        Comment comment = getCommentById(commentId);

        if (!comment.getTask().getId().equals(taskId)) {
            throw new BusinessException(ErrorCode.TASK_NOT_FOUND);
        }

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        comment.delete();
        log.debug("댓글 삭제: commentId={}, taskId={}", commentId, taskId);
    }
} 