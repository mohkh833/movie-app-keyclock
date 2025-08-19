import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { MovieRoutingModule } from './movie-routing.module';
import { MainComponent } from './pages/main/main.component';
import { MenuComponent } from './components/menu/menu.component';
import { MoviesListComponent } from './pages/movies-list/movies-list.component';
import { MovieCardComponent } from './components/movie-card/movie-card.component';
import { RatingComponent } from './components/rating/rating.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import { MovieDetailsComponent } from './pages/movie-details/movie-details.component';


@NgModule({
  declarations: [
    MainComponent,
    MenuComponent,
    MoviesListComponent,
    MovieCardComponent,
    RatingComponent,
    ManageBookComponent,
    MovieDetailsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MovieRoutingModule
  ]
})
export class MovieModule { }
