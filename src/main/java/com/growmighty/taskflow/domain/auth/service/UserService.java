package com.growmighty.taskflow.domain.auth.service;

import com.growmighty.taskflow.common.exception.BusinessException;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.domain.auth.dto.UserResponse;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UserResponse request, Long currentUserId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.getId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.updateName(request.getName());
        }

        return UserResponse.from(user);
    }
} 