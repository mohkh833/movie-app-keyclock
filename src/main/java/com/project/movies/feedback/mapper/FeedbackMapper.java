package com.project.movies.feedback.mapper;

import com.project.movies.feedback.types.Feedback;
import com.project.movies.feedback.types.FeedbackRequest;
import com.project.movies.feedback.types.FeedbackResponse;
import com.project.movies.movie.types.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {


    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .movie(Movie.builder()
                        .id(request.movieId())
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .build();
    }
}
