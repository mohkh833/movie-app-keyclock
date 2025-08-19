package com.project.movies.feedback;

import com.project.movies.common.types.PageResponse;
import com.project.movies.feedback.types.FeedbackRequest;
import com.project.movies.feedback.types.FeedbackResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;


    @PostMapping
    @PreAuthorize("hasRole('ADD_FEEDBACK')")
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @DeleteMapping("/{feedback-id}")
    @PreAuthorize("hasRole('DELETE_FEEDBACK')")
    public ResponseEntity<Integer> deleteFeedback(
            @PathVariable("feedback-id") Integer feedbackId,
            Authentication connectedUser
    ) {
        service.deleteFeedBack(feedbackId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movie/{movie-id}")
    @PreAuthorize("hasRole('VIEW_FEEDBACK')")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
            @PathVariable("movie-id") Integer movieId,
            @RequestParam(name = "page" , defaultValue = "0", required = false) int page,
            @RequestParam(name ="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.findAllFeedbacksByMovie(movieId, page, size, connectedUser));
    }
}
