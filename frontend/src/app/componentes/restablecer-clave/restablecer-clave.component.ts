import {HttpErrorResponse} from '@angular/common/http';
import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {ActivatedRoute, RouterLink} from '@angular/router';

import {AuthApiService} from '../../servicios/auth-api.service';

@Component({
  selector: 'app-restablecer-clave',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './restablecer-clave.component.html',
  styleUrl: './restablecer-clave.component.css',
})
export class RestablecerClaveComponent implements OnInit {
  readonly minPasswordLength = 6;

  private authApi = inject(AuthApiService);
  private route = inject(ActivatedRoute);

  token = '';
  newPassword = '';
  confirmPassword = '';
  loading = signal(false);
  success = signal(false);
  errorMessage = signal('');

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
  }

  submitResetPassword(form: NgForm) {
    if (this.loading() || form.invalid) {
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage.set('Las contraseñas no coinciden.');
      return;
    }

    if (!this.token) {
      this.errorMessage.set('Token inválido. Solicita un nuevo enlace de recuperación.');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.authApi.resetPassword(this.token, this.newPassword).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(true);
      },
      error: (error: HttpErrorResponse) => {
        this.loading.set(false);
        if (error.status === 0) {
          this.errorMessage.set('No se pudo conectar con el servidor.');
        } else if (error.error?.message) {
          this.errorMessage.set(error.error.message);
        } else {
          this.errorMessage.set('El enlace es inválido o ha expirado. Solicita uno nuevo.');
        }
      },
    });
  }
}
