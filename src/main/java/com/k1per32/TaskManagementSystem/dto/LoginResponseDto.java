package com.k1per32.TaskManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public final class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String scope;
    private Long expiresIn;
    private String error;
}