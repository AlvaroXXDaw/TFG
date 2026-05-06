import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {
  CancelCheckoutSessionRequest,
  CheckoutSessionResponse,
  CreateCheckoutSessionRequest,
} from '../modelos/payment.models';

@Injectable({providedIn: 'root'})
export class PaymentsApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/payments`;

  createCheckoutSession(payload: CreateCheckoutSessionRequest): Observable<CheckoutSessionResponse> {
    return this.http.post<CheckoutSessionResponse>(`${this.baseUrl}/create-checkout-session`, payload);
  }

  cancelCheckoutSession(stripeSessionId: string): Observable<void> {
    const payload: CancelCheckoutSessionRequest = {stripeSessionId};
    return this.http.post<void>(`${this.baseUrl}/cancel-checkout-session`, payload);
  }
}
