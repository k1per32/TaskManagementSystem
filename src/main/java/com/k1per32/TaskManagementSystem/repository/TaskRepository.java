package com.k1per32.TaskManagementSystem.repository;


import com.k1per32.TaskManagementSystem.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByAuthorIdNotLike(String authorId);
    Page<Task> findAllByAuthorId(String authorId, Pageable pageable);
    Optional<Task> findFirstByAuthorId(String authorId);

    Optional<Task> findFirstByPerformerId(String performerId);
    Page<Task> findAllByPerformerId(String performerId, Pageable pageable);
 }