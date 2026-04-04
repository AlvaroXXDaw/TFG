export type ClientPlan = 'BASIC' | 'PREMIUM';

export interface Client {
  id: string;
  name: string;
  email: string;
  plan: ClientPlan;
  role: string;
  joinDate: string;
  phone: string;
  birthDate: string;
}

export interface CreateClientRequest {
  name: string;
  email: string;
  plan: ClientPlan;
  password: string;
  phone: string;
  birthDate: string;
}

export interface UpdateClientPlanRequest {
  plan: ClientPlan;
}
