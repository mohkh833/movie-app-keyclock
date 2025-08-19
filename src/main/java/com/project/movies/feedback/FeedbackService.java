package com.project.movies.feedback;

import com.project.movies.common.types.PageResponse;
import com.project.movies.feedback.mapper.FeedbackMapper;
import com.project.movies.feedback.repository.FeedbackRepository;
import com.project.movies.feedback.types.Feedback;
import com.project.movies.feedback.types.FeedbackRequest;
import com.project.movies.feedback.types.FeedbackResponse;
import com.project.movies.movie.repository.MovieRepository;
import com.project.movies.movie.types.Movie;
import com.project.movies.user.repository.UserRepository;
import com.project.movies.user.types.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final MovieRepository movieRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
         movieRepository.findById(request.movieId())
                .orElseThrow(() -> new EntityNotFoundException("No movie found with the ID: "+ request.movieId()));

        Feedback feedback = feedbackMapper.toFeedback(request);

        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByMovie(Integer movieId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = getUser(connectedUser);
        movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("No movie found with the ID: "+ movieId));
        Page<Feedback> feedbacks = feedbackRepository.findAllByMovieId(movieId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }

    public void deleteFeedBack(Integer feedbackId, Authentication connectedUser) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                        .orElseThrow(() -> new EntityNotFoundException("Feedback with ID " + feedbackId + " not found"));

        feedbackRepository.delete(feedback);
    }

    private User getUser(Authentication connectedUser) {
        Jwt jwt = (Jwt) connectedUser.getPrincipal();
        String email = jwt.getClaim("email");

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
