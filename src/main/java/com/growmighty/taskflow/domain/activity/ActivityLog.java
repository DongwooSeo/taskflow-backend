package com.growmighty.taskflow.domain.activity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String requestMethod;

    @Column(nullable = false)
    private String requestUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    @Column(nullable = true)
    private Long targetId;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ActivityLog(Long userId, String ipAddress, String requestMethod, String requestUrl,
                       ActivityType activityType, Long targetId) {
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.activityType = activityType;
        this.targetId = targetId;
    }
} 