package com.project.movies.auth.mapper;

import com.project.movies.auth.types.AuthenticationResponse;
import com.project.movies.auth.types.RegistrationRequest;
import com.project.movies.user.types.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationMapper {

    private final PasswordEncoder passwordEncoder;

//    public User mapToUserEntity(RegistrationRequest request, Role userRole) {
//        return User.builder()
//                .firstname(request.getFirstname())
//                .lastname(request.getLastname())
//                .email(request.getEmail())
//                .build();
//    }
//
//    public AuthenticationResponse maptoAuthenticationResponse(User user, String jwtToken) {
//        return AuthenticationResponse.builder()
//                .fullname(user.fullName())
//                .email(user.getEmail())
//                .token(jwtToken)
//                .build();
//    }
}
