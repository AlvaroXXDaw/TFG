import {SportType} from './reservation.models';

export interface CreateCheckoutSessionRequest {
  sport: SportType;
  courtId: string;
  date: string;
  time: string;
}

export interface CheckoutSessionResponse {
  reservationId: string;
  stripeSessionId: string;
  checkoutUrl: string;
}

export interface CancelCheckoutSessionRequest {
  stripeSessionId: string;
}
