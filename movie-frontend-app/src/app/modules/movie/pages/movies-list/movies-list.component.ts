import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import { MovieResponse, PageResponseMovieResponse } from 'src/app/services/models';
import { MovieControllerService } from 'src/app/services/services';
import {Router} from '@angular/router';
@Component({
  selector: 'app-movies-list',
  templateUrl: './movies-list.component.html',
  styleUrls: ['./movies-list.component.scss']
})
export class MoviesListComponent implements OnInit{
  movieResponse:PageResponseMovieResponse = {};
  page =0;
  size = 5;
  pages: any = [];
  searchText: string = '';
  message = '';
  level: 'success' |'error' = 'success';

  constructor(
    private movieService: MovieControllerService,
    private router: Router
  ) {

  }

  ngOnInit(): void {
    this.findAllMovies();
  }

  @Output() keyword = new EventEmitter<string>();

  private findAllMovies(){
    this.movieService.findAllMovies({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (movies) => {
        this.movieResponse = movies;
        this.pages = Array(this.movieResponse.totalPage).fill(0).map((x,i)=>i);
      }
    });
  }
  public searchMovie() {
    this.movieService.searchMovies({
      keyword: this.searchText,  // Ensure keyword is passed
      page: this.page,
      size: this.size
    }).subscribe({
      next: (movies) => {
        this.movieResponse = movies;
        this.pages = Array(this.movieResponse.totalPage).fill(0).map((x,i)=>i);
      },
      error: (err) => {
        console.error('Error fetching movies:', err);
      }
    });
  }

  public onSearch() {
    this.searchMovie();
  }
  

  gotToPage(page: number) {
    this.page = page;
    this.findAllMovies();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllMovies();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllMovies();
  }

  goToLastPage() {
    this.page = this.movieResponse.totalPage as number - 1;
    this.findAllMovies();
  }

  goToNextPage() {
    this.page++;
    this.findAllMovies();
  }

  get isLastPage() {
    return this.page === this.movieResponse.totalPage as number - 1;
  }

  displayMovieDetails(movie: MovieResponse) {
    this.router.navigate(['movies', 'details', movie.id]);
  }

}
