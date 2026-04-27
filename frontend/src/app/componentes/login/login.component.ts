import {HttpErrorResponse} from '@angular/common/http';
import {Component, inject, signal} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';

import {AuthStore} from '../../auth.store';
import {AuthSession} from '../../modelos/auth.models';
import {AuthApiService} from '../../servicios/auth-api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  readonly minPasswordLength = 6;

  private authApi = inject(AuthApiService);
  private authStore = inject(AuthStore);
  private router = inject(Router);

  email = '';
  password = '';
  showPassword = false;
  errorMessage = signal('');
  loading = signal(false);

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  submitLogin(form: NgForm) {
    if (this.loading() || form.invalid) {
      return;
    }

    this.runLogin(this.email, this.password);
  }

  private runLogin(email: string, password: string) {
    this.loading.set(true);
    this.errorMessage.set('');

    this.authApi.login({
      email: email.trim().toLowerCase(),
      password,
    }).subscribe({
      next: (session) => {
        this.handleSuccess(session);
      },
      error: (error: HttpErrorResponse) => {
        this.handleError(error);
      },
    });
  }

  private handleSuccess(session: AuthSession) {
    this.authStore.setSession(session);
    this.loading.set(false);

    const targetRoute = session.role === 'ADMIN' ? '/admin' : '/';
    this.router.navigate([targetRoute], { replaceUrl: true });
  }

  private handleError(error: HttpErrorResponse) {
    this.loading.set(false);

    if (error.status === 0) {
      this.errorMessage.set('No se pudo conectar con el backend. Revisa si esta levantado.');
      return;
    }

    if (error.status === 400 || error.status === 401) {
      this.errorMessage.set('Email o contraseña incorrectos.');
      return;
    }

    this.errorMessage.set('No se pudo iniciar sesión. Inténtalo de nuevo.');
  }
}
