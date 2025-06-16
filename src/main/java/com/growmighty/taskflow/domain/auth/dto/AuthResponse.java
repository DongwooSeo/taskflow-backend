package com.growmighty.taskflow.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.growmighty.taskflow.domain.auth.entity.Role;
import com.growmighty.taskflow.domain.auth.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String name;
    private final Role role;
    private final String token;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime createdAt;

    private AuthResponse(Long id, String username, String email, String name, Role role, String token, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
        this.token = token;
        this.createdAt = createdAt;
    }

    public static AuthResponse fromUser(User user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                null,
                user.getCreatedAt()
        );
    }

    public static AuthResponse withToken(String token) {
        return new AuthResponse(
                null, null, null, null, null,
                token,
                null
        );
    }

    public static AuthResponse fromUserWithToken(User user, String token) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                token,
                user.getCreatedAt()
        );
    }
} 