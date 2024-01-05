package com.k1per32.TaskManagementSystem.controller;

import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.exception.RestApiException;
import com.k1per32.TaskManagementSystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Авторизация",
            tags = {"Task service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto,
                                              @AuthenticationPrincipal Jwt principal) {
        return Optional.of(taskDto)
                .map(taskDto1 -> taskService.createTask(taskDto1, principal.getSubject()))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }

    @Operation(
            summary = "Авторизация",
            tags = {"Task service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> editTask(@PathVariable Integer id,
                                            @RequestBody TaskDto taskDto) {

        return Optional.of(id)
                .map(taskService::getTaskDtoById)
                .map(taskDto1 -> taskService.editTask(taskDto, id))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }

    @Operation(
            summary = "Авторизация",
            tags = {"Task service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = List.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @GetMapping()
    public ResponseEntity<List<TaskDto>> getAllTasks() {

        return Optional.of(taskService.getListTaskDto())
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }

    @Operation(
            summary = "Авторизация",
            tags = {"Task service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id) {

        Optional.of(id)
                .map(taskService::getTaskDtoById)
                .ifPresent(task -> taskService.deleteTask(id));
    }

    @Operation(
            summary = "Авторизация",
            tags = {"Task service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> changeStatus(@PathVariable Integer id,
                                                @AuthenticationPrincipal Jwt principal,
                                                @QueryParam("status") String status) {

        return Optional.of(status)
                .map(status1 -> taskService.changeStatus(id, status1, principal.getSubject()))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }

    @Operation(
            summary = "Авторизация",
            tags = {"Task service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping("/{id}")
    public ResponseEntity<TaskDto> appointPerformer(@PathVariable Integer id,
                                                    @AuthenticationPrincipal Jwt principal,
                                                    @QueryParam("performerId") String performerId) {

        return Optional.of(performerId)
                .map(performerId1 -> taskService.appointPerformer(id, performerId1, principal.getSubject()))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }

}