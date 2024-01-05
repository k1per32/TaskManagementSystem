package com.k1per32.TaskManagementSystem.dto;


import com.k1per32.TaskManagementSystem.entity.Task;
import com.k1per32.TaskManagementSystem.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CommentDto {
    private Integer id;
    private String comment;
}
