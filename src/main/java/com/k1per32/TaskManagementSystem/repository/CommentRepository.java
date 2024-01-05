package com.k1per32.TaskManagementSystem.repository;

import com.k1per32.TaskManagementSystem.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
