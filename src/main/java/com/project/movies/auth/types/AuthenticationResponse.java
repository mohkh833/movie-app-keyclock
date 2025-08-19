package com.project.movies.auth.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private String fullname;
    private String email;
    private String role;
}
