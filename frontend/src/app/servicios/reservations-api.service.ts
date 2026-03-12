import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {
  AvailabilitySlot,
  CreateMaintenanceBlockRequest,
  CreateReservationRequest,
  Reservation,
  ReservationStatus,
  SportType,
} from '../modelos/reservation.models';

@Injectable({providedIn: 'root'})
export class ReservationsApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getAll(filters?: {sport?: SportType; status?: ReservationStatus; date?: string}): Observable<Reservation[]> {
    let params = new HttpParams();

    if (filters?.sport) params = params.set('sport', filters.sport);
    if (filters?.status) params = params.set('status', filters.status);
    if (filters?.date) params = params.set('date', filters.date);

    return this.http.get<Reservation[]>(`${this.baseUrl}/reservations`, {params});
  }

  create(payload: CreateReservationRequest): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.baseUrl}/reservations`, payload);
  }

  createMaintenance(payload: CreateMaintenanceBlockRequest): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.baseUrl}/reservations/maintenance`, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/reservations/${id}`);
  }

  getAvailability(courtId: string, date: string): Observable<AvailabilitySlot[]> {
    const params = new HttpParams().set('courtId', courtId).set('date', date);
    return this.http.get<AvailabilitySlot[]>(`${this.baseUrl}/availability`, {params});
  }
}
