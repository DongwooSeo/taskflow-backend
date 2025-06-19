package com.growmighty.taskflow.domain.comment.controller;

import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.comment.dto.CommentRequest;
import com.growmighty.taskflow.domain.comment.dto.CommentResponse;
import com.growmighty.taskflow.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        log.debug("댓글 생성 요청: taskId={}, userId={}", taskId, user.getId());
        CommentResponse response = commentService.createComment(taskId, request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("댓글이 생성되었습니다.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
            @PathVariable Long taskId,
            Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        log.debug("댓글 목록 조회 요청: taskId={}", taskId);
        Page<CommentResponse> response = commentService.getComments(taskId, pageable);
        return ResponseEntity.ok(ApiResponse.success("댓글 목록을 조회했습니다.", response));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        log.debug("댓글 수정 요청: taskId={}, commentId={}", taskId, commentId);
        CommentResponse response = commentService.updateComment(taskId, commentId, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다.", response));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        log.debug("댓글 삭제 요청: taskId={}, commentId={}", taskId, commentId);
        commentService.deleteComment(taskId, commentId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", null));
    }
} 