import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest, AuthenticationResponse } from 'src/app/services/models';
import { AuthenticationControllerService } from 'src/app/services/services';
import { TokenService } from 'src/app/services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  authRequest:  AuthenticationRequest = {
    email: '',
    password: ''
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationControllerService,
    private tokenService: TokenService
  ) { }
    
  login() {
    this.errorMsg = [];
    this.authService.authenticate( {
      body: this.authRequest
    }).subscribe({
      next: (res: AuthenticationResponse) => {
        this.tokenService.token = res.token as string;
        const isAdmin = res?.role?.includes('ADMIN') ?? false;
        this.tokenService.isAdmin = isAdmin;
        if (isAdmin) {
          this.router.navigate(['movies/manage']); // Redirect to the admin page
        } else {
          this.router.navigate(['movies']); // Redirect to a regular user page
        }
      },
      error: (err) => {
        if(err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors
        } else { 
          this.errorMsg.push(err.error.error);
        }
      }
    });
  }

  register() {
    this.router.navigate(['register']);
  }
}


