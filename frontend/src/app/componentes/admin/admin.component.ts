import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthStore } from '../../auth.store';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent {
  auth = inject(AuthStore);
  router = inject(Router);
  isMobileMenuOpen = signal(false);

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
