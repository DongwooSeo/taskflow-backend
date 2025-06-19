package com.growmighty.taskflow.domain.comment.entity;

import com.growmighty.taskflow.common.entity.BaseEntity;
import com.growmighty.taskflow.domain.auth.entity.User;
import com.growmighty.taskflow.domain.task.entity.Task;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Comment createComment(String content, Task task, User user) {
        Comment comment = new Comment();
        comment.content = content;
        comment.task = task;
        comment.user = user;
        return comment;
    }

    public void update(String content) {
        this.content = content;
    }
} 