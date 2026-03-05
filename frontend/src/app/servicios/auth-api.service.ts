import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {AuthSession, LoginRequest} from '../modelos/auth.models';

@Injectable({providedIn: 'root'})
export class AuthApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/auth`;

  login(payload: LoginRequest): Observable<AuthSession> {
    return this.http.post<AuthSession>(`${this.baseUrl}/login`, payload);
  }

  forgotPassword(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/forgot-password`, {email});
  }

  resetPassword(token: string, newPassword: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/reset-password`, {token, newPassword});
  }

  register(name: string, email: string, password: string, phone: string, birthDate: string): Observable<AuthSession> {
    return this.http.post<AuthSession>(`${this.baseUrl}/register`, {name, email, password, phone, birthDate});
  }
}
