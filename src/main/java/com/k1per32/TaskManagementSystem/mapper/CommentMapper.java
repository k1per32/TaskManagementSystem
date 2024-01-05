package com.k1per32.TaskManagementSystem.mapper;

import com.k1per32.TaskManagementSystem.dto.CommentDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.Comment;
import com.k1per32.TaskManagementSystem.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {
    Comment convertToComment(CommentDto commentDto);
    CommentDto convertToCommentDto(Comment comment);
}
