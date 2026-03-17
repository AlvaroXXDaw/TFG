import {Routes} from '@angular/router';
import {LayoutComponent} from './componentes/layout/layout.component';
import {AdminComponent} from './componentes/admin/admin.component';
import {adminOnlyGuard} from './guards/admin-only.guard';
import {authRequiredGuard} from './guards/auth-required.guard';
import {guestOnlyGuard} from './guards/guest-only.guard';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        loadComponent: () => import('./componentes/inicio/inicio.component').then((m) => m.InicioComponent),
      },
      {
        path: 'reservar',
        canActivate: [authRequiredGuard],
        loadComponent: () => import('./componentes/reservas/reservas.component').then((m) => m.ReservasComponent),
      },
      {
        path: 'gimnasio',
        canActivate: [authRequiredGuard],
        loadComponent: () => import('./componentes/gimnasio/gimnasio.component').then((m) => m.GimnasioComponent),
      },
      {
        path: 'perfil',
        canActivate: [authRequiredGuard],
        loadComponent: () => import('./componentes/perfil/perfil.component').then((m) => m.PerfilComponent),
      },

      {
        path: 'instalaciones/:facilityId',
        loadComponent: () => import('./componentes/instalaciones/instalaciones.component').then((m) => m.InstalacionesComponent),
      },
    ],
  },
  {
    path: 'admin',
    canActivate: [adminOnlyGuard],
    component: AdminComponent,
    children: [
      {path: '', redirectTo: 'reservas', pathMatch: 'full'},
      {
        path: 'reservas',
        loadComponent: () => import('./componentes/admin-reservas/admin-reservas.component').then((m) => m.AdminReservasComponent),
      },
      {
        path: 'clientes',
        loadComponent: () => import('./componentes/admin-clientes/admin-clientes.component').then((m) => m.AdminClientesComponent),
      },
      {
        path: 'pistas',
        loadComponent: () => import('./componentes/admin-pistas/admin-pistas.component').then((m) => m.AdminPistasComponent),
      },
    ],
  },
  {
    path: 'login',
    canActivate: [guestOnlyGuard],
    loadComponent: () => import('./componentes/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'recuperar-clave',
    loadComponent: () => import('./componentes/recuperar-clave/recuperar-clave.component').then((m) => m.RecuperarClaveComponent),
  },
  {
    path: 'restablecer-clave',
    loadComponent: () => import('./componentes/restablecer-clave/restablecer-clave.component').then((m) => m.RestablecerClaveComponent),
  },
  {
    path: 'registro',
    canActivate: [guestOnlyGuard],
    loadComponent: () => import('./componentes/registro/registro.component').then((m) => m.RegistroComponent),
  },
  {path: '**', redirectTo: ''},
];
