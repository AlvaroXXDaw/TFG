import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {ProfileSummary} from '../modelos/profile.models';

@Injectable({providedIn: 'root'})
export class ProfileApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/profile`;

  getByClient(clientId: string): Observable<ProfileSummary> {
    return this.http.get<ProfileSummary>(`${this.baseUrl}/${clientId}`);
  }
}
