import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { MovieResponse } from 'src/app/services/models';

@Component({
  selector: 'app-movie-card',
  templateUrl: './movie-card.component.html',
  styleUrls: ['./movie-card.component.scss']
})
export class MovieCardComponent {
  private _movie: MovieResponse = {};
  private _manage = false;
  private _movieCover: string | undefined;

  constructor(private router:Router) {
    
  }

  get movieCover(): string | undefined {
    if (this._movie.poster) {
      return  this._movie.poster;
    }
    return 'https://media.istockphoto.com/id/1033704156/vector/loading-circle-icon-progress-loading-vector-icon-update-icon.jpg?s=612x612&w=0&k=20&c=Ap_ELUDTrZj1jRFqbqBfKipF6Y_4C8QRgwWf0NFLDw0=';
  }



  get movie(): MovieResponse {
    return this._movie;
  }

  @Input()
  set movie(value: MovieResponse) {
    this._movie = value;
  } 

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private details: EventEmitter<MovieResponse> = new EventEmitter<MovieResponse>();


  onShowDetails() {
    this.details.emit(this._movie);
    this.router.navigate(['/movies/details/', this._movie.id]); 
  }

  
}
