package com.project.movies.movie;

import com.project.movies.auth.KeyclockService;
import com.project.movies.common.types.PageResponse;
import com.project.movies.movie.mapper.MovieMapper;
import com.project.movies.movie.repository.MovieRepository;
import com.project.movies.movie.types.Movie;
import com.project.movies.movie.types.MovieRequest;
import com.project.movies.movie.types.MovieResponse;
import com.project.movies.user.repository.UserRepository;
import com.project.movies.user.types.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final MovieMapper movieMapper;
    private final KeyclockService keyclockService;

    public MovieResponse save(MovieRequest request, Authentication connectedUser) {
        User user = getUser(connectedUser);
        Movie movie = movieMapper.toMovie(request);
        movie.setOwner(user);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(savedMovie);
    }

    public List<MovieResponse> saveBatch(List<MovieRequest> requests, Authentication connectedUser) {
        User user = getUser(connectedUser);

        List<Movie> movies = requests.stream()
                .map(request -> {
                    Movie movie = movieMapper.toMovie(request);
                    movie.setOwner(user);
                    return movie;
                }).collect(Collectors.toList());

        List<Movie> savedMovies = movieRepository.saveAll(movies);

        return savedMovies.stream()
                .map(movieMapper::toMovieResponse)
                .collect(Collectors.toList());

    }

    public void deleteBatch(List<String> requests, Authentication connectedUser) {
        User user = getUser(connectedUser);

        List<Integer> movieIds = requests.stream().map(Integer::parseInt).collect(Collectors.toList());

        List<Movie> deletedMovies = movieRepository.findAllById(movieIds).stream()
                .filter(movie -> movie.getOwner().getId().equals(user.getId()))
                .collect(Collectors.toList());

        movieRepository.deleteAll(deletedMovies);

    }

    public void deleteMovie(Integer movieId, Authentication connectedUser) {
//        User user = getUser(connectedUser);


        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie with ID " + movieId + " not found"));

//        if (!movie.getOwner().getId().equals(user.getId()) || !user.get) {
//            throw new AccessDeniedException("You are not authorized to delete this movie");
//        }

        movieRepository.delete(movie);
    }

    public MovieResponse findById(Integer movieId) {
        return movieRepository.findById(movieId)
                .map(movieMapper::toMovieResponse)
                .orElseThrow(() -> new EntityNotFoundException("No Movie found with the ID: " + movieId));
    }

    public PageResponse<MovieResponse> findAllMovies(int page, int size, Authentication connectedUser) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Movie> moviesPage = movieRepository.findAllDisplayableMovies(pageable);

        List<MovieResponse> movieResponses = moviesPage.getContent()
                .stream()
                .map(movieMapper::toMovieResponse)
                .toList();

        return new PageResponse<>(
                movieResponses,
                moviesPage.getNumber(),
                moviesPage.getSize(),
                moviesPage.getTotalElements(),
                moviesPage.getTotalPages(),
                moviesPage.isFirst(),
                moviesPage.isLast()
        );
    }

    public PageResponse<MovieResponse> searchMovies(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Movie> moviesPage = movieRepository.searchMovies(keyword, pageable);
        List<MovieResponse> movieResponses = moviesPage.stream()
                .map(movieMapper::toMovieResponse)
                .toList();

        return new PageResponse<>(
                movieResponses,
                moviesPage.getNumber(),
                moviesPage.getSize(),
                moviesPage.getTotalElements(),
                moviesPage.getTotalPages(),
                moviesPage.isFirst(),
                moviesPage.isLast()
        );
    }

    private User getUser(Authentication connectedUser) {
        Jwt jwt = (Jwt) connectedUser.getPrincipal();
        String email = jwt.getClaim("email");

//        String adminToken = keyclockService.fetchAdminAccessToken();
//        UserRepresentation user = keyclockService.getUserByUsername(username, adminToken);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}
