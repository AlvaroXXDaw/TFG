import {HttpErrorResponse} from '@angular/common/http';
import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';

import {AuthStore} from '../../auth.store';
import {AuthApiService} from '../../servicios/auth-api.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css',
})
export class RegistroComponent {
  private authApi = inject(AuthApiService);
  private authStore = inject(AuthStore);
  private router = inject(Router);

  name = '';
  email = '';
  password = '';
  phone = '';
  birthDate = '';
  errorMessage = signal('');
  loading = signal(false);

  submitRegister() {
    if (this.loading() || !this.name || !this.email || !this.password || !this.phone || !this.birthDate) {
      return;
    }

    if (this.authStore.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }
    if (this.password.length < 6) {
      this.errorMessage.set('La contraseña debe tener al menos 6 caracteres.');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.authApi.register(
      this.name.trim(),
      this.email.trim().toLowerCase(),
      this.password,
      this.phone.trim(),
      this.birthDate
    ).subscribe({
      next: (session) => {
        this.authStore.setSession(session);
        this.loading.set(false);
        this.router.navigate(['/'], { replaceUrl: true });
      },
      error: (error: HttpErrorResponse) => {
        this.loading.set(false);
        if (error.status === 0) {
          this.errorMessage.set('No se pudo conectar con el servidor.');
        } else if (error.error?.message) {
          this.errorMessage.set(error.error.message);
        } else {
          this.errorMessage.set('No se pudo crear la cuenta. Es posible que el email ya esté registrado.');
        }
      },
    });
  }
}
