package com.project.movies.movie;

import com.project.movies.common.types.PageResponse;
import com.project.movies.movie.types.MovieRequest;
import com.project.movies.movie.types.MovieResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @PostMapping
    @PreAuthorize("hasRole('ADD_MOVIES')")
    public ResponseEntity<MovieResponse> saveMovie(
            @Valid @RequestBody MovieRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADD_MOVIES')")
    public ResponseEntity<List<MovieResponse>> saveMoviesBatch(
             @RequestBody List<@Valid MovieRequest> requests,
            Authentication connectedUser
            ) {
        return ResponseEntity.ok(service.saveBatch(requests, connectedUser));
    }

    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('DELETE_MOVIES')")
    public ResponseEntity<List<MovieResponse>> deleteMoviesBatch(
            @RequestBody List<String> requests,
            Authentication connectedUser
    ) {
        service.deleteBatch(requests, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{movie-id}")
    @PreAuthorize("hasRole('VIEW_MOVIES')")
    public ResponseEntity<MovieResponse> findMovieById(
            @PathVariable("movie-id") Integer movieId
    ) {
        return ResponseEntity.ok(service.findById(movieId));
    }

    @DeleteMapping("/{movie-id}")
    @PreAuthorize("hasRole('DELETE_MOVIES')")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable("movie-id") Integer movieId,
            Authentication connectedUser
    ) {
        service.deleteMovie(movieId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('VIEW_MOVIES')")  // ✅ Single role
    public ResponseEntity<PageResponse<MovieResponse>> findAllMovies(
            @RequestParam(name ="page", defaultValue = "0", required = false) int page,
            @RequestParam(name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllMovies(page, size, connectedUser));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('VIEW_MOVIES')")
    public ResponseEntity<PageResponse<MovieResponse>> searchMovies(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return  ResponseEntity.ok(service.searchMovies(keyword,page, size));
    }
}
