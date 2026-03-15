import {Injectable, signal} from '@angular/core';

@Injectable({providedIn: 'root'})
export class ApiErrorStore {
  readonly message = signal<string | null>(null);

  set(message: string) {
    this.message.set(message);
  }

  clear() {
    this.message.set(null);
  }
}
