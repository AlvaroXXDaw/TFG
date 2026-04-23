import {computed, inject, Injectable, PLATFORM_ID, signal} from '@angular/core';
import {isPlatformBrowser} from '@angular/common';

import {AuthSession, UserRole} from './modelos/auth.models';

const STORAGE_KEY = 'oasisclub.auth.session';
const VALID_ROLES: UserRole[] = ['ADMIN', 'MEMBER', 'EMPLOYEE'];

@Injectable({providedIn: 'root'})
export class AuthStore {
  private readonly isBrowser = isPlatformBrowser(inject(PLATFORM_ID));
  private readonly _session = signal<AuthSession | null>(this.loadFromStorage());

  readonly session = this._session.asReadonly();
  readonly isLoggedIn = computed(() => this._session() !== null);
  readonly isAdmin = computed(() => this._session()?.role === 'ADMIN');
  readonly isUserRole = computed(() => {
    const role = this._session()?.role;
    return role ? VALID_ROLES.includes(role) : false;
  });

  setSession(session: AuthSession) {
    const normalized = this.normalizeSession(session);
    if (!normalized) {
      this.logout();
      return;
    }

    this._session.set(normalized);
    if (this.isBrowser) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(normalized));
    }
  }

  logout() {
    this._session.set(null);
    if (this.isBrowser) {
      localStorage.removeItem(STORAGE_KEY);
    }
  }

  private loadFromStorage(): AuthSession | null {
    if (!this.isBrowser) {
      return null;
    }

    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }

    try {
      const parsed = JSON.parse(raw);
      const session = this.normalizeSession(parsed);
      if (!session) {
        localStorage.removeItem(STORAGE_KEY);
        return null;
      }

      return session;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }

  private normalizeSession(value: unknown): AuthSession | null {
    if (!value || typeof value !== 'object') {
      return null;
    }

    const candidate = value as Partial<AuthSession>;
    if (
      typeof candidate.token !== 'string' ||
      !candidate.token.trim() ||
      typeof candidate.clientId !== 'string' ||
      !candidate.clientId.trim() ||
      typeof candidate.name !== 'string' ||
      !candidate.name.trim() ||
      typeof candidate.email !== 'string' ||
      !candidate.email.trim() ||
      typeof candidate.role !== 'string' ||
      !VALID_ROLES.includes(candidate.role as UserRole)
    ) {
      return null;
    }

    return {
      token: candidate.token,
      clientId: candidate.clientId,
      name: candidate.name.trim(),
      email: candidate.email.trim().toLowerCase(),
      role: candidate.role as UserRole,
    };
  }
}
