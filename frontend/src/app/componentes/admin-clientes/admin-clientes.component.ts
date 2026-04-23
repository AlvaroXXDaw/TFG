import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ClientsApiService } from '../../servicios/clients-api.service';
import { Client, CreateClientRequest } from '../../modelos/client.models';

@Component({
  selector: 'app-admin-clientes',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-clientes.component.html',
  styleUrl: './admin-clientes.component.css',
})
export class AdminClientesComponent implements OnInit {
  private clientsApi = inject(ClientsApiService);
  readonly resultLimit = 10;

  clients = signal<Client[]>([]);
  loading = signal(true);
  search = signal('');

  newClient = {
    name: '',
    email: '',
    password: '',
    phone: '',
    birthDate: '',
  };

  ngOnInit() {
    this.loadClients();
  }

  private loadClients() {
    this.loading.set(true);
    this.clientsApi.getAll(this.search(), this.resultLimit).subscribe({
      next: (clients) => {
        this.clients.set(clients);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  applySearch(event: Event) {
    event.preventDefault();
    this.loadClients();
  }

  addClient(event: Event) {
    event.preventDefault();
    if (!this.newClient.name || !this.newClient.email || !this.newClient.password) return;

    const request: CreateClientRequest = {
      name: this.newClient.name.trim(),
      email: this.newClient.email.trim().toLowerCase(),
      password: this.newClient.password,
      phone: this.newClient.phone.trim(),
      birthDate: this.newClient.birthDate,
    };

    this.clientsApi.create(request).subscribe({
      next: () => {
        this.newClient.name = '';
        this.newClient.email = '';
        this.newClient.password = '';
        this.newClient.phone = '';
        this.newClient.birthDate = '';
        this.loadClients();
      },
    });
  }

  removeClient(id: string) {
    this.clientsApi.delete(id).subscribe({
      next: () => {
        this.loadClients();
      },
    });
  }
}
