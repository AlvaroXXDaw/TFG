import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthStore } from '../../auth.store';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  auth = inject(AuthStore);
  private router = inject(Router);
  isMobileMenuOpen = signal(false);

  logout() {
    this.auth.logout();
    this.isMobileMenuOpen.set(false);
    this.router.navigate(['/']);
  }
}
