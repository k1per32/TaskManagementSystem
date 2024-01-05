package com.k1per32.TaskManagementSystem.repository;

import com.k1per32.TaskManagementSystem.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Integer> {
    UserTask findByTaskId(Integer taskId);
}