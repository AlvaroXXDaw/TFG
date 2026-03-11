import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {RoutineDay, UpdateRoutineRequest} from '../modelos/gym.models';

@Injectable({providedIn: 'root'})
export class GymApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/gym/routines`;

  getByClient(clientId: string): Observable<RoutineDay[]> {
    return this.http.get<RoutineDay[]>(`${this.baseUrl}/${clientId}`);
  }

  update(clientId: string, payload: UpdateRoutineRequest): Observable<RoutineDay[]> {
    return this.http.put<RoutineDay[]>(`${this.baseUrl}/${clientId}`, payload);
  }
}
