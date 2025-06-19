package com.growmighty.taskflow.domain.comment.repository;

import com.growmighty.taskflow.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.task " +
            "WHERE c.task.id = :taskId AND c.deleted = false")
    Page<Comment> findAllByTaskId(@Param("taskId") Long taskId, Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.task " +
            "WHERE c.id = :id AND c.deleted = false")
    Optional<Comment> findByIdAndDeletedFalse(@Param("id") Long id);
} 