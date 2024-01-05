package com.k1per32.TaskManagementSystem.mapper;


import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {
    Task convertToTask(TaskDto taskDto);
    TaskDto convertToTaskDto(Task task);
}