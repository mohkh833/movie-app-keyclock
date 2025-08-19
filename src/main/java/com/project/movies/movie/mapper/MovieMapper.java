package com.project.movies.movie.mapper;

import com.project.movies.movie.types.Movie;
import com.project.movies.movie.types.MovieRequest;
import com.project.movies.movie.types.MovieResponse;
import org.springframework.stereotype.Service;

@Service
public class MovieMapper {
    public Movie toMovie(MovieRequest request) {
        return  Movie.builder()
                .id(request.id())
                .title(request.title())
                .poster(request.posterUrl())
                .Released(request.Released())
                .build();
    }

    public MovieResponse toMovieResponse (Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .poster(movie.getPoster())
                .Released(movie.getReleased())
                .rate(movie.getRate())
                .build();
    }
}
