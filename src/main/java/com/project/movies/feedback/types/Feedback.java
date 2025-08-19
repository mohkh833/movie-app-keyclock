package com.project.movies.feedback.types;


import com.project.movies.common.types.BaseEntity;
import com.project.movies.movie.types.Movie;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {
    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

}
