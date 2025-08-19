import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MovieResponse } from 'src/app/services/models';
import { MovieControllerService } from 'src/app/services/services';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss']
})
export class ManageBookComponent implements OnInit {

  searchTerm: string = '';
  movies: any[] = [];
  savedMovies: MovieResponse[] = [];
  private omdbApiUrl = 'https://www.omdbapi.com/';
  private apiKey = '4e2bcd38'; // Replace with your OMDB API key

  constructor(
    private http: HttpClient,
    private movieService: MovieControllerService
  ) {}

  ngOnInit(): void {
    this.loadSavedMovies();
  }

  searchMovies(): void {
    if (this.searchTerm.trim()) {
      this.http
        .get(`/api/omdb/?s=${this.searchTerm}&apikey=${this.apiKey}`)
        .subscribe((response: any) => {
          this.movies = response.Search || [];
        });
    }
  }

  saveMovie(movie: any): void {
    const movieData = {
      title: movie.Title,
      year: movie.Year,
      posterUrl: movie.Poster,
      Released: "true",
    };

    this.movieService.saveMovie({ body: movieData }).subscribe(() => {
      alert('Movie saved successfully!');
      this.loadSavedMovies();
    });
  }

  loadSavedMovies(): void {
    this.movieService.findAllMovies().subscribe(response => {
      this.savedMovies = response.content || [];
    });
  }

  deleteMovie(movieId: any): void {
    this.movieService.deleteMovie({ 'movie-id': movieId }).subscribe(() => {
      alert('Movie deleted successfully!');
      this.loadSavedMovies();
    });
  }

  
}
