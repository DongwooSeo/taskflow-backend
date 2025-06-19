package com.growmighty.taskflow.domain.task.repository;

import com.growmighty.taskflow.domain.task.entity.Status;
import com.growmighty.taskflow.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT DISTINCT t FROM Task t " +
            "LEFT JOIN FETCH t.assignee " +
            "LEFT JOIN FETCH t.creator " +
            "WHERE t.deleted = false AND " +
            "(:status is null OR t.status = :status) AND " +
            "(:assigneeId is null OR t.assignee.id = :assigneeId) AND " +
            "(:search is null OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> findAllByFilters(
            @Param("status") Status status,
            @Param("assigneeId") Long assigneeId,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.assignee " +
            "LEFT JOIN FETCH t.creator " +
            "WHERE t.id = :id AND t.deleted = false")
    Optional<Task> findByIdAndDeletedFalse(@Param("id") Long id);
} 