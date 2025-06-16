package com.growmighty.taskflow.domain.auth.controller;

import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.common.util.ResponseUtil;
import com.growmighty.taskflow.domain.auth.dto.UserResponse;
import com.growmighty.taskflow.domain.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(HttpServletRequest request) {
        log.debug("getCurrentUser called");
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.warn("UserId is null in getCurrentUser");
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        UserResponse user = userService.getCurrentUser(userId);
        log.debug("getCurrentUser returning user response: {}", user);
        return ResponseUtil.ok(user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(HttpServletRequest request) {
        String userRole = (String) request.getAttribute("userRole");
        if (!"ADMIN".equals(userRole)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        List<UserResponse> users = userService.getAllUsers();
        return ResponseUtil.ok(users);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserResponse request,
            HttpServletRequest servletRequest
    ) {
        Long userId = (Long) servletRequest.getAttribute("userId");
        String userRole = (String) servletRequest.getAttribute("userRole");
        
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        if (!userId.equals(id) && !"ADMIN".equals(userRole)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        UserResponse updatedUser = userService.updateUser(id, request, userId);
        return ResponseUtil.ok(updatedUser);
    }
} 