import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ScheduleApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getSlots(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/schedule`);
  }

  addSlot(time: string): Observable<{ time: string }> {
    return this.http.post<{ time: string }>(`${this.baseUrl}/schedule`, { time });
  }

  removeSlot(time: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/schedule/${time}`);
  }
}
