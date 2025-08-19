import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './pages/main/main.component';
import { MoviesListComponent } from './pages/movies-list/movies-list.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import { MovieDetailsComponent } from './pages/movie-details/movie-details.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    children: [
      {
        path: '',
        component: MoviesListComponent
      },
      {
        path: 'manage',
        component: ManageBookComponent
      },
      { path: 'details/:id', component: MovieDetailsComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MovieRoutingModule { }
