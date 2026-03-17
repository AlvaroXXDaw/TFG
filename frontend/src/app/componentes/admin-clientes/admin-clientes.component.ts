import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ClientsApiService } from '../../servicios/clients-api.service';
import { Client, ClientPlan, CreateClientRequest } from '../../modelos/client.models';

@Component({
  selector: 'app-admin-clientes',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-clientes.component.html',
  styleUrl: './admin-clientes.component.css',
})
export class AdminClientesComponent implements OnInit {
  private clientsApi = inject(ClientsApiService);
  readonly plans: ClientPlan[] = ['BASIC', 'PREMIUM'];
  readonly resultLimit = 10;

  clients = signal<Client[]>([]);
  loading = signal(true);
  search = signal('');
  planDrafts = signal<Record<string, ClientPlan>>({});
  savingPlanId = signal<string | null>(null);

  newClient = {
    name: '',
    email: '',
    plan: 'BASIC' as ClientPlan,
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
        this.rebuildPlanDrafts(clients);
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
      plan: this.newClient.plan,
      password: this.newClient.password,
      phone: this.newClient.phone.trim(),
      birthDate: this.newClient.birthDate,
    };

    this.clientsApi.create(request).subscribe({
      next: () => {
        this.newClient.name = '';
        this.newClient.email = '';
        this.newClient.password = '';
        this.newClient.plan = 'BASIC';
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

  onPlanDraftChange(clientId: string, value: string) {
    if (value !== 'BASIC' && value !== 'PREMIUM') {
      return;
    }

    this.planDrafts.update((drafts) => ({
      ...drafts,
      [clientId]: value,
    }));
  }

  getDraftPlan(client: Client): ClientPlan {
    const draft = this.planDrafts()[client.id];
    return draft ?? client.plan;
  }

  hasPlanChange(client: Client) {
    return this.getDraftPlan(client) !== client.plan;
  }

  applyClientPlan(client: Client) {
    if (client.role !== 'MEMBER') {
      return;
    }

    const nextPlan = this.getDraftPlan(client);
    if (client.plan === nextPlan) {
      return;
    }

    this.savingPlanId.set(client.id);
    this.clientsApi.updateClientPlan(client.id, nextPlan).subscribe({
      next: (updatedClient) => {
        this.clients.update((list) =>
          list.map((current) => (current.id === updatedClient.id ? updatedClient : current)),
        );
        this.planDrafts.update((drafts) => ({
          ...drafts,
          [updatedClient.id]: updatedClient.plan,
        }));
        this.savingPlanId.set(null);
      },
      error: () => {
        this.savingPlanId.set(null);
        this.loadClients();
      },
    });
  }

  private rebuildPlanDrafts(clients: Client[]) {
    const drafts: Record<string, ClientPlan> = {};
    for (const client of clients) {
      if (client.role === 'MEMBER') {
        drafts[client.id] = client.plan;
      }
    }

    this.planDrafts.set(drafts);
  }
}
