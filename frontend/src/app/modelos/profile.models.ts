export interface ProfileReservation {
  id: string;
  sport: string;
  court: string;
  date: string;
  time: string;
  status: string;
}

export interface ProfileSummary {
  clientId: string;
  name: string;
  reservations: ProfileReservation[];
}
