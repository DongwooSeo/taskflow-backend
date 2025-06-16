package com.growmighty.taskflow.domain.auth.dto;

import com.growmighty.taskflow.domain.auth.entity.Role;
import com.growmighty.taskflow.domain.auth.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final Role role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
} 