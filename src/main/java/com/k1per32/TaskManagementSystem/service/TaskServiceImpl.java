package com.k1per32.TaskManagementSystem.service;

import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.Task;
import com.k1per32.TaskManagementSystem.entity.UserTask;
import com.k1per32.TaskManagementSystem.exception.RestApiException;
import com.k1per32.TaskManagementSystem.mapper.TaskMapper;
import com.k1per32.TaskManagementSystem.repository.TaskRepository;
import com.k1per32.TaskManagementSystem.repository.UserRepository;
import com.k1per32.TaskManagementSystem.repository.UserTaskRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class TaskServiceImpl implements TaskService {
    private final TaskMapper mapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;

    @Override
    public TaskDto createTask(TaskDto taskDto, String id) {
        Task taskEntity = new Task();
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setPriority(taskDto.getPriority());
        taskEntity.setStatus(taskDto.getStatus());
        taskEntity.setAuthorId(id);
        taskRepository.save(taskEntity);
        UserTask userTask = new UserTask();
        userTask.setTaskId(taskEntity.getId());
        userTask.setUserId(id);
        userTaskRepository.save(userTask);
        return mapper.convertToTaskDto(taskEntity);
    }

    @Override
    public TaskDto getTaskDtoById(Integer id) {
        Task taskEntity = taskRepository.findById(3)
                .orElseThrow(() -> new RestApiException(404, "Задача не найдена"));
        return mapper.convertToTaskDto(taskEntity);

    }

    @Override
    public TaskDto editTask(TaskDto taskDto, Integer id) {
        Task taskEntity = taskRepository.getReferenceById(id);
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setPriority(taskDto.getPriority());
        taskEntity.setStatus(taskDto.getStatus());
        taskRepository.save(taskEntity);
        return mapper.convertToTaskDto(taskEntity);
    }

    @Override
    public List<TaskDto> getListTaskDto() {
        return taskRepository.findAll().stream()
                .map(mapper::convertToTaskDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Integer id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Задачи с таким id не существует");
        } else {
            userTaskRepository.deleteById(userTaskRepository.findByTaskId(id).getId());
            taskRepository.deleteById(id);
        }

    }

    @Override
    public TaskDto changeStatus(Integer taskId, String status, String id) {
        Task taskEntity = taskRepository.getReferenceById(taskId);
        taskEntity.setStatus(status.toUpperCase());
        taskRepository.save(taskEntity);
        return mapper.convertToTaskDto(taskEntity);
    }

    @Override
    public TaskDto appointPerformer(Integer taskId, String performerId, String id) {
        Task taskEntity = taskRepository.getReferenceById(taskId);
        taskEntity.setPerformerId(performerId);
        taskRepository.save(taskEntity);
        return mapper.convertToTaskDto(taskEntity);
    }
}
