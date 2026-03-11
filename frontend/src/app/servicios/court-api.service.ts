import {Injectable, inject} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {CourtResponse, CreateCourtRequest} from '../modelos/court.model';

@Injectable({
  providedIn: 'root',
})
export class CourtApiService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/courts`;

  getCourts(sport?: 'PADEL' | 'FUTBOL'): Observable<CourtResponse[]> {
    let params = new HttpParams();
    if (sport) {
      params = params.set('sport', sport);
    }
    return this.http.get<CourtResponse[]>(this.apiUrl, { params });
  }

  createCourt(request: CreateCourtRequest): Observable<CourtResponse> {
    return this.http.post<CourtResponse>(this.apiUrl, request);
  }

  deleteCourt(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
