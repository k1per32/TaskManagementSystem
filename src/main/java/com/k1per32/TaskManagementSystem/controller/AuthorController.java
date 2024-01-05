package com.k1per32.TaskManagementSystem.controller;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.k1per32.TaskManagementSystem.dto.LoginRequestDto;
import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.dto.SignUpDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.User;
import com.k1per32.TaskManagementSystem.exception.RestApiException;
import com.k1per32.TaskManagementSystem.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(
            summary = "Регистрация",
            tags = {"Author service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SignUpDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "409", description = "Email already exist", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping(path = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SignUpDto> signUp(@RequestBody SignUpDto signUpRequest) {
        return ResponseEntity.ok(authorService.signUp(signUpRequest));
    }

    @Operation(
            summary = "Авторизация",
            tags = {"Author service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LoginResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping(path = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        return ResponseEntity.ok(authorService.login(loginDto));
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
            summary = "Просмотр задач других авторов",
            tags = {"Author service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = List.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "404", description = "Page Not Found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @GetMapping()
    public ResponseEntity<List<TaskDto>> getAllTasksOtherAuthors
            (@AuthenticationPrincipal Jwt principal) {

        return Optional.of(authorService.getAllTasksOtherAuthors(principal.getSubject()))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }

    @Operation(
            summary = "Просмотр задач конкретного автора, а также все комментарии к ним",
            tags = {"Author service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Page.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "404", description = "Page Not Found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @GetMapping("/{page}")
    public ResponseEntity<Page<TaskDto>> getAllTasksSpecificAuthorAndAllComments
            (@PathVariable Integer page,
             @RequestBody User user) {

        return Optional.of(user)
                .map(user1 -> authorService.getAllTasksSpecificAuthorAndAllComments(user, page))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }
}