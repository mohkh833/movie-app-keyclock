package com.project.movies.auth;
import com.project.movies.auth.types.AuthenticationRequest;
import com.project.movies.auth.types.RegistrationRequest;
import com.project.movies.auth.types.TokenResponse;
import com.project.movies.config.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class KeyclockService {

    private final KeycloakProperties keycloakProperties;
    private final WebClient webClient = WebClient.builder().build();
    private static final String CONTENT_TYPE_FORM = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
    private static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;

    public UserRepresentation getUserByUsername(String username, String adminToken) {
        List<UserRepresentation> users = webClient.get()
                .uri(keycloakProperties.getAuthServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users?username=" + username)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .retrieve()
                .bodyToFlux(UserRepresentation.class)
                .collectList()
                .block();

        return users.isEmpty() ? null : users.get(0);
    }

    public void validateUser(UserRepresentation user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        if (!Boolean.TRUE.equals(user.isEmailVerified())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email is not verified");
        }
    }

    public TokenResponse getTokenResponse(AuthenticationRequest request) {
        return webClient.post()
                .uri(keycloakProperties.getAuthServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_FORM)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", keycloakProperties.getResource())
                        .with("client_secret", keycloakProperties.getCredentials().getSecret())
                        .with("username", request.getUser())
                        .with("password", request.getPassword()))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    public String fetchAdminAccessToken() {
        TokenResponse token = webClient.post()
                .uri(keycloakProperties.getAuthServerUrl() + "/realms/master/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_FORM)
                .bodyValue("grant_type=password&client_id=admin-cli" +
                        "&username=admin" +
                        "&password=admin")
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        return token.getAccess_token();
    }

    public String createUser(RegistrationRequest request, String adminToken) {
        Map<String, Object> payload = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "enabled", true,
                "firstName", request.getFirstname(),
                "lastName", request.getLastname(),
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", request.getPassword(),
                        "temporary", false
                ))
        );

        ResponseEntity<Void> response = webClient.post()
                .uri(keycloakProperties.getAuthServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users")
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .block();

        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (location == null || !location.contains("/")) {
            throw new RuntimeException("Failed to retrieve user ID from Keycloak response");
        }
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public void assignRole(String userId, String adminToken, String role) {
        String baseUrl = keycloakProperties.getAuthServerUrl();
        String realm = keycloakProperties.getRealm();

        Map<String, Object> roleData = webClient.get()
                .uri(baseUrl + "/admin/realms/" + realm + "/roles/" + role)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        webClient.post()
                .uri(baseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm")
                .header(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .bodyValue(List.of(roleData))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void sendVerificationEmail(String userId, String adminToken) {
        webClient.put()
                .uri(keycloakProperties.getAuthServerUrl() + "/admin/realms/" + keycloakProperties.getRealm() + "/users/" + userId + "/send-verify-email")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void sendResetPasswordEmail(String userId, String adminToken) {
        webClient.put()
                .uri(keycloakProperties.getAuthServerUrl() +
                        "/admin/realms/" + keycloakProperties.getRealm() +
                        "/users/" + userId + "/execute-actions-email")
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .bodyValue(List.of("UPDATE_PASSWORD")) // Actions to trigger
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
