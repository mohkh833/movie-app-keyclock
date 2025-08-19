import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FeedbackResponse, MovieResponse } from 'src/app/services/models';
import { FeedbackControllerService, MovieControllerService } from 'src/app/services/services';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.scss']
})
export class MovieDetailsComponent {
  movieId: string | null = null;
  public movie: MovieResponse = {};
  feedback: FeedbackResponse ={};
  feedbacks: FeedbackResponse[] = [];
  

  constructor(
    private route: ActivatedRoute,
    private movieService: MovieControllerService,
    private feedbackService: FeedbackControllerService
  ) {}

  ngOnInit() {
    this.movieId = this.route.snapshot.paramMap.get('id');
    if (this.movieId) {
      this.fetchMovieDetails(this.movieId);
      this.getMovieFeedback(this.movieId);
    }
  }

  get movieCover(): string | undefined {
    if (this.movie?.poster) {
      return  this.movie.poster;
    }
    return 'https://media.istockphoto.com/id/1033704156/vector/loading-circle-icon-progress-loading-vector-icon-update-icon.jpg?s=612x612&w=0&k=20&c=Ap_ELUDTrZj1jRFqbqBfKipF6Y_4C8QRgwWf0NFLDw0=';
  }
  fetchMovieDetails(movieId: string) {
    this.movieService.findMovieById({ 'movie-id': +movieId }).subscribe({
      next: (res) => this.movie = res,
      error: (err) => console.error('Failed to fetch movie details', err)
    });
  }

  getMovieFeedback(movieId: string) {
    this.feedbackService.findAllFeedbackByBook({ 'movie-id': +movieId, page: 0, size: 10 }).subscribe({
      next: (res) => this.feedbacks = res.content ?? [],
      error: (err) => console.error('❌ Failed to fetch feedback', err)
    });
  }

  submitFeedback() {
    const movieId = this.movie?.id;
    const feedbackRequest = {
      note: this.feedback.note ?? 0,
      comment: this.feedback.comment ?? '',
      movieId: movieId ?? 0
    };

    this.feedbackService.saveFeedback({body:feedbackRequest}).subscribe(() => {
      this.getMovieFeedback(movieId?.toString() ?? ''); // Refresh feedback list
      this.feedback = { note: 0, comment: '' }; // Reset form
    });
  }
}
