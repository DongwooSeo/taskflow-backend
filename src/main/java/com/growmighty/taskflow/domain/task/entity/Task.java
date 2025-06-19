package com.growmighty.taskflow.domain.task.entity;

import com.growmighty.taskflow.common.entity.BaseEntity;
import com.growmighty.taskflow.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.TODO;

    public static Task createTask(String title, String description, Priority priority, 
                                User assignee, User creator, LocalDateTime dueDate) {
        Task task = new Task();
        task.title = title;
        task.description = description;
        task.priority = priority;
        task.assignee = assignee;
        task.creator = creator;
        task.dueDate = dueDate;
        return task;
    }

    public void update(String title, String description, Priority priority, 
                      User assignee, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }

    public void updateStatus(Status newStatus) {
        if (this.status != newStatus) {
            if (newStatus == Status.IN_PROGRESS && this.startedAt == null) {
                this.startedAt = LocalDateTime.now();
            }
            this.status = newStatus;
        }
    }

    public void assignTo(User assignee) {
        this.assignee = assignee;
    }
} 