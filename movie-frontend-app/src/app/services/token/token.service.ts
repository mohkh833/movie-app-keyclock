import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }
  get token() {
    return localStorage.getItem('token') as string;
  }

  set isAdmin(isAdmin: boolean) {
    localStorage.setItem('isAdmin', isAdmin.toString());
  }

  get isAdmin() {
    return JSON.parse(localStorage.getItem('isAdmin') || 'false');
  }

  clear() {
    localStorage.removeItem('token');
    localStorage.removeItem('isAdmin');
  }
}
