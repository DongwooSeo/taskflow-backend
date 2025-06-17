package com.growmighty.taskflow.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growmighty.taskflow.common.dto.ApiResponse;
import com.growmighty.taskflow.common.exception.ErrorCode;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/signup",
            "/api/health",
            "/swagger-ui",
            "/v3/api-docs",
            "/h2-console"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return request.getMethod().equals("OPTIONS") ||
                PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            log.debug("Processing request to '{}' with JWT: {}", request.getRequestURI(), jwt != null ? "present" : "absent");

            if (!StringUtils.hasText(jwt)) {
                log.debug("No JWT token found in request");
                sendErrorResponse(response, ErrorCode.UNAUTHORIZED);
                return;
            }

            if (!tokenProvider.validateToken(jwt)) {
                log.debug("Invalid JWT token");
                sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
                return;
            }

            Long userId = tokenProvider.getUserIdFromJWT(jwt);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Set userId in request attributes
            request.setAttribute("userId", userId);
            request.setAttribute("userRole", user.getRole().name());

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("Could not set user authentication", ex);
            sendErrorResponse(response, ErrorCode.UNAUTHORIZED);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.error(errorCode.getMessage());
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
} 