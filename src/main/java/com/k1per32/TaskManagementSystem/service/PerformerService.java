package com.k1per32.TaskManagementSystem.service;

import com.k1per32.TaskManagementSystem.dto.LoginRequestDto;
import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.dto.SignUpDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PerformerService {

    SignUpDto signUp(SignUpDto signUpDto);

    LoginResponseDto login(LoginRequestDto loginRequest);

    Page<TaskDto> getAllTasksSpecificAuthorAndAllComments(User user, Integer page);
}