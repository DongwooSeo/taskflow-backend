package com.growmighty.taskflow.domain.auth.controller;

import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.common.util.ResponseUtil;
import com.growmighty.taskflow.domain.auth.dto.UserResponse;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal User user
    ) {
        log.debug("getCurrentUser called");
        UserResponse response = userService.getCurrentUser(user.getId());
        log.debug("getCurrentUser returning user response: {}", response);
        return ResponseUtil.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @AuthenticationPrincipal User user
    ) {
//        if (!user.getRole().name().equals("ADMIN")) {
//            throw new BusinessException(ErrorCode.FORBIDDEN);
//        }
        List<UserResponse> users = userService.getAllUsers();
        return ResponseUtil.ok(users);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserResponse request,
            @AuthenticationPrincipal User user
    ) {
        if (!user.getId().equals(id) && !user.getRole().name().equals("ADMIN")) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        UserResponse updatedUser = userService.updateUser(id, request, user.getId());
        return ResponseUtil.ok(updatedUser);
    }
} 