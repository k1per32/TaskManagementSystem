package com.k1per32.TaskManagementSystem.service;

import com.k1per32.TaskManagementSystem.dto.LoginRequestDto;
import com.k1per32.TaskManagementSystem.dto.LoginResponseDto;
import com.k1per32.TaskManagementSystem.dto.SignUpDto;
import com.k1per32.TaskManagementSystem.dto.TaskDto;
import com.k1per32.TaskManagementSystem.entity.Task;
import com.k1per32.TaskManagementSystem.entity.User;
import com.k1per32.TaskManagementSystem.exception.RestApiException;
import com.k1per32.TaskManagementSystem.mapper.TaskMapper;

import com.k1per32.TaskManagementSystem.repository.TaskRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PerformerServiceImpl implements PerformerService {
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private final String clientId = "performer_client";

    private final String role = "performer";

    private final String clientSecret = "4sRD7hhTr0bHHmVtutB4QaRLMqROI5my";

    @Override
    public SignUpDto signUp(SignUpDto signUpDto) {
        LOGGER.info("signUp... {}", signUpDto);
        Keycloak keycloak =
                KeycloakBuilder.builder()
                        .serverUrl(authServerUrl)
                        .grantType(OAuth2Constants.PASSWORD)
                        .realm("master")
                        .clientId("admin-cli")
                        .username("user")
                        .password("bitnami")
                        .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build()).build();

        keycloak.tokenManager().getAccessToken();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(signUpDto.getEmail());
        user.setFirstName(signUpDto.getFirstname());
        user.setLastName(signUpDto.getLastname());
        user.setEmail(signUpDto.getEmail());

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersRessource = realmResource.users();

        Response response = usersRessource.create(user);

        signUpDto.setStatusCode(response.getStatus());
        signUpDto.setStatusMessage(response.getStatusInfo().toString());

        if (response.getStatus() == 201) {

            String userId = CreatedResponseUtil.getCreatedId(response);

            LOGGER.info("Created userId {}", userId);

            // create password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(signUpDto.getPassword());

            UserResource userResource = usersRessource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);

            // Get realm role author
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();

            // Assign realm role performer to user
            userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));

        }
        return signUpDto;

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");

        Configuration configuration =
                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);

        AccessTokenResponse response =
                authzClient.obtainAccessToken(loginRequest.getEmail(), loginRequest.getPassword());
        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setAccessToken(response.getToken());
        loginResponse.setRefreshToken(response.getRefreshToken());
        loginResponse.setScope(response.getScope());
        loginResponse.setExpiresIn(response.getExpiresIn());
        return loginResponse;
    }

    @Override
    public Page<TaskDto> getAllTasksSpecificAuthorAndAllComments(User user, Integer page) {
        taskRepository.findFirstByPerformerId(user.getId()).orElseThrow(() -> new RestApiException(400, "Неправильно введены данные пользователя"));
        Page<Task> pageTaskEntity =  taskRepository.findAllByPerformerId(user.getId(),
                PageRequest.of(page, 5, Sort.by("priority")));
        return pageTaskEntity.map(mapper::convertToTaskDto);
    }

}