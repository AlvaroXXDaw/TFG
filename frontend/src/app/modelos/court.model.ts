export interface CourtResponse {
  id: string;
  name: string;
  sport: 'PADEL' | 'FUTBOL';
  isActive: boolean;
  createdAt: string;
}

export interface CreateCourtRequest {
  name: string;
  sport: 'PADEL' | 'FUTBOL';
}
