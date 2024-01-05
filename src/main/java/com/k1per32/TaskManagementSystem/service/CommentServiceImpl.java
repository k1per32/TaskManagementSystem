package com.k1per32.TaskManagementSystem.service;

import com.k1per32.TaskManagementSystem.dto.CommentDto;
import com.k1per32.TaskManagementSystem.entity.Comment;
import com.k1per32.TaskManagementSystem.entity.Task;
import com.k1per32.TaskManagementSystem.entity.User;
import com.k1per32.TaskManagementSystem.mapper.CommentMapper;
import com.k1per32.TaskManagementSystem.repository.CommentRepository;
import com.k1per32.TaskManagementSystem.repository.TaskRepository;
import com.k1per32.TaskManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentMapper mapper;
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto createComment(Integer taskId, CommentDto commentDto, String id) {
        Comment commentEntity = new Comment();
        commentEntity.setComment(commentDto.getComment());
        Task taskEntity = taskRepository.getReferenceById(taskId);
        User userEntity = userRepository.getReferenceById(id);
        userEntity.addCommentToUser(commentEntity);
        taskEntity.addCommentToTask(commentEntity);
        taskRepository.save(taskEntity);
        userRepository.save(userEntity);
        commentRepository.save(commentEntity);
        return mapper.convertToCommentDto(commentEntity);
    }
}
