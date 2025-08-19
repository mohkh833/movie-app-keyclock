package com.project.movies.auth;

import com.project.movies.auth.types.*;
import com.project.movies.config.KeycloakProperties;
import com.project.movies.security.JwtService;
import com.project.movies.user.repository.UserRepository;
import com.project.movies.user.types.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.account.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final KeyclockService keyclockService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public AuthenticationResponse register(RegistrationRequest request) {
        try {
            String adminToken = keyclockService.fetchAdminAccessToken();

            String userId = keyclockService.createUser(request, adminToken);
            keyclockService.assignRole(userId, adminToken, request.getRole());
            keyclockService.sendVerificationEmail(userId, adminToken);

            User user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email(request.getEmail()).build();

            userRepository.save(user);

            return AuthenticationResponse.builder()
                    .fullname(request.getFirstname() + " " + request.getLastname())
                    .email(request.getEmail())
                    .role(request.getRole())
                    .build();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 409) {
                throw new IllegalArgumentException("Email is already in use");
            }
            throw new RuntimeException("Keycloak registration failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during registration: " + e.getMessage());
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            String adminToken = keyclockService.fetchAdminAccessToken();

            UserRepresentation user = keyclockService.getUserByUsername(request.getUser(), adminToken);
            keyclockService.validateUser(user);

            TokenResponse tokenResponse = keyclockService.getTokenResponse(request);

            List<String> roles = jwtService.getUserRoles(tokenResponse.getAccess_token());
            String fullname = jwtService.getFullName(tokenResponse.getAccess_token());

            log.info("User [{}] authenticated successfully. Full name: [{}], Roles: {}, Email: [{}]",
                    request.getUser(), fullname, roles, user.getEmail());

            return AuthenticationResponse.builder()
                    .token(tokenResponse.getAccess_token())
                    .fullname(jwtService.getFullName(tokenResponse.getAccess_token()))
                    .email(request.getUser())
                    .role(jwtService.getUserRole(tokenResponse.getAccess_token()))
                    .build();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 400 || e.getStatusCode().value() == 401) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            }
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error during authentication: " + ex.getMessage());
        }
    }

    public String sendResetPasswordEmail(String username) {
        String adminToken = keyclockService.fetchAdminAccessToken();
        UserRepresentation user = keyclockService.getUserByUsername(username, adminToken);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        try {
            keyclockService.sendResetPasswordEmail(user.getId(), adminToken);
            return "Password reset email sent successfully to " + username;
        } catch (WebClientResponseException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send password reset email: " + e.getResponseBodyAsString()
            );
        }
    }


}
