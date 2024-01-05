package com.k1per32.TaskManagementSystem.dto;

import com.k1per32.TaskManagementSystem.entity.Comment;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class TaskDto {

    private Integer id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String authorId;
    private String performerId;
    private List<Comment> comments;
}
