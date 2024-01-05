package com.k1per32.TaskManagementSystem.controller;

import com.k1per32.TaskManagementSystem.dto.CommentDto;
import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.exception.RestApiException;
import com.k1per32.TaskManagementSystem.service.CommentService;
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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Создание комментария к конкретной задаче авторизованным пользователем",
            tags = {"Comment service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid Request Body", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvalidCredentialsException.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestApiException.class))))})
    @PostMapping("/{taskId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Integer taskId,
                                                 @RequestBody CommentDto commentDto,
                                                 @AuthenticationPrincipal Jwt principal) {
        return Optional.of(commentDto)
                .map(commentDto1 -> commentService.createComment(taskId, commentDto1, principal.getSubject()))
                .map(ResponseEntity::ok)
                .orElseThrow(RestApiException::new);
    }
}
