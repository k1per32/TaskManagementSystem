package com.k1per32.TaskManagementSystem.service;


import com.k1per32.TaskManagementSystem.dto.LoginRequestDto;
import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.dto.SignUpDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface AuthorService {

    SignUpDto signUp(SignUpDto signUpDto);

    LoginResponseDto login(LoginRequestDto loginRequest);

    List<TaskDto> getAllTasksOtherAuthors(String id);

    Page<TaskDto> getAllTasksSpecificAuthorAndAllComments(User user, Integer page);
}