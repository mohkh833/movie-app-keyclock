package com.project.movies.auth.types;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordRequest {
    @NotEmpty(message = "User is mandatory")
    @NotBlank(message = "User is mandatory")
    private String user;
}

