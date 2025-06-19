package com.growmighty.taskflow.domain.auth.controller;

import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.common.util.ResponseUtil;
import com.growmighty.taskflow.domain.auth.dto.UserResponse;
import com.growmighty.taskflow.domain.auth.service.UserService;
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
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @RequestAttribute Long userId
    ) {
        log.debug("getCurrentUser called");
        UserResponse user = userService.getCurrentUser(userId);
        log.debug("getCurrentUser returning user response: {}", user);
        return ResponseUtil.ok(user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
//        String userRole = (String) request.getAttribute("userRole");
//        if (!"ADMIN".equals(userRole)) {
//            throw new BusinessException(ErrorCode.FORBIDDEN);
//        }
        List<UserResponse> users = userService.getAllUsers();
        return ResponseUtil.ok(users);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserResponse request,
            @RequestAttribute Long userId,
            @RequestAttribute String userRole
    ) {
        if (!userId.equals(id) && !"ADMIN".equals(userRole)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        UserResponse updatedUser = userService.updateUser(id, request, userId);
        return ResponseUtil.ok(updatedUser);
    }
} 