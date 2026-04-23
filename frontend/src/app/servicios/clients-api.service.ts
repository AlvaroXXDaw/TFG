import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {
  Client,
  CreateClientRequest,
} from '../modelos/client.models';

@Injectable({providedIn: 'root'})
export class ClientsApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiBaseUrl}/clients`;

  getAll(query?: string, limit = 10): Observable<Client[]> {
    let params = new HttpParams().set('limit', String(limit));
    if (query && query.trim()) {
      params = params.set('query', query.trim());
    }
    return this.http.get<Client[]>(this.baseUrl, {params});
  }

  create(payload: CreateClientRequest): Observable<Client> {
    return this.http.post<Client>(this.baseUrl, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
