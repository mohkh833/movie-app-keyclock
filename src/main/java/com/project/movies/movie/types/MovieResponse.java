package com.project.movies.movie.types;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {
    private Integer id;
    private String title;
    private String poster;
    private String Released;
    private double rate;

}
