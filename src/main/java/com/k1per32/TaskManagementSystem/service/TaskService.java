package com.k1per32.TaskManagementSystem.service;

import com.k1per32.TaskManagementSystem.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto, String id);

    TaskDto getTaskDtoById(Integer id);

    TaskDto editTask(TaskDto taskDto, Integer id);

    List<TaskDto> getListTaskDto();

    void deleteTask(Integer id);

    TaskDto changeStatus(Integer taskId, String status, String id);

    TaskDto appointPerformer(Integer taskId, String performerId, String id);
}
