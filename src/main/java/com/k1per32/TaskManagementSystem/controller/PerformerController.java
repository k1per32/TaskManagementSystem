package com.k1per32.TaskManagementSystem.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.k1per32.TaskManagementSystem.dto.LoginRequestDto;
import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.dto.SignUpDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.User;
import com.k1per32.TaskManagementSystem.exception.RestApiException;
import com.k1per32.TaskManagementSystem.service.PerformerService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/performers")
public class PerformerController {
    private final TaskService taskService;
    private final PerformerService performerService;

    @Operation(
            summary = "Регистрация",
            tags = {"Performer service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SignUpDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "409", description = "Email already exist", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping(path = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SignUpDto> signUp(@RequestBody SignUpDto signUpRequest) {
        return ResponseEntity.ok(performerService.signUp(signUpRequest));
    }
    @Operation(
            summary = "Авторизация",
            tags = {"Performer service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LoginResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        return ResponseEntity.ok(performerService.login(loginDto));
    }
    @Operation(
            summary = "Тест авторизированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @GetMapping(path = "/data")
    public ResponseEntity<?> data() {
        return ResponseEntity.ok(Arrays.asList("Hello world!"));
    }

    @Operation(
            summary = "Изменение статуса задачи",
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
            summary = "Просмотр задач конкретного исполнителя, а также все комментарии к ним",
            tags = {"Performer service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Page.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "404", description = "Page Not Found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @GetMapping("/{page}")
    public ResponseEntity<Page<TaskDto>> getAllTasksSpecificPerformerAndAllComments
            (@PathVariable Integer page,
             @RequestBody User user) {

        return Optional.of(user)
                .map(user1 -> performerService.getAllTasksSpecificAuthorAndAllComments(user, page))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }
}