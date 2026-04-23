export type UserRole = 'ADMIN' | 'MEMBER' | 'EMPLOYEE';

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
}
