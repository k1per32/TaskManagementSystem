package com.k1per32.TaskManagementSystem.service;

import com.k1per32.TaskManagementSystem.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(Integer taskId, CommentDto commentDto, String id);
}
