package com.project.movies.auth;

import com.project.movies.auth.types.AuthenticationRequest;
import com.project.movies.auth.types.AuthenticationResponse;
import com.project.movies.auth.types.RegistrationRequest;
import com.project.movies.auth.types.ResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register (
            @RequestBody @Valid RegistrationRequest request
    ) {
        AuthenticationResponse response =  service.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    public record MessageResponse(String message) {}

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword (
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        String msg = service.sendResetPasswordEmail(request.getUser());
        return ResponseEntity.ok(new MessageResponse(msg));
    }
}
