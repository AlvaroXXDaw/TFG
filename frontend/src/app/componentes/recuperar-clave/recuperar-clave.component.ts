import {HttpErrorResponse} from '@angular/common/http';
import {Component, inject, signal} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {RouterLink} from '@angular/router';

import {AuthApiService} from '../../servicios/auth-api.service';

@Component({
  selector: 'app-recuperar-clave',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './recuperar-clave.component.html',
  styleUrl: './recuperar-clave.component.css',
})
export class RecuperarClaveComponent {
  private authApi = inject(AuthApiService);

  email = '';
  loading = signal(false);
  sent = signal(false);
  errorMessage = signal('');

  submitForgotPassword(form: NgForm) {
    if (this.loading() || form.invalid) {
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.authApi.forgotPassword(this.email.trim().toLowerCase()).subscribe({
      next: () => {
        this.loading.set(false);
        this.sent.set(true);
      },
      error: (error: HttpErrorResponse) => {
        this.loading.set(false);
        if (error.status === 0) {
          this.errorMessage.set('No se pudo conectar con el servidor.');
        } else if (error.error?.message) {
          this.errorMessage.set(error.error.message);
        } else {
          this.errorMessage.set('Ha ocurrido un error. Inténtalo de nuevo.');
        }
      },
    });
  }
}
