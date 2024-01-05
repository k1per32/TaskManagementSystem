package com.k1per32.TaskManagementSystem.dto;

import com.k1per32.TaskManagementSystem.entity.Comment;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class UserDto {
    private String id;
    private String email;
    private List<Comment> comments;
}
