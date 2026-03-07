export type UserRole = 'ADMIN' | 'MEMBER' | 'EMPLOYEE';
export type ClientPlan = 'BASIC' | 'PREMIUM';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthSession {
  token: string;
  clientId: string;
  name: string;
  email: string;
  role: UserRole;
  plan: ClientPlan;
}
