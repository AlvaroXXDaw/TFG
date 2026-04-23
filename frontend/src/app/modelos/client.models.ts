export interface Client {
  id: string;
  name: string;
  email: string;
  role: string;
  joinDate: string;
  phone: string;
  birthDate: string;
}

export interface CreateClientRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  birthDate: string;
}
