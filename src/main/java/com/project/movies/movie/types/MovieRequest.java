package com.project.movies.movie.types;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record MovieRequest(
        Integer id,

        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String title,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String posterUrl,

        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String year,

        @NotNull(message = "104")
        @NotEmpty(message = "104")
        String Released



) {
}
