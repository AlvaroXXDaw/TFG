import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CourtResponse } from '../../modelos/court.model';
import { CreateMaintenanceBlockRequest, Reservation, SportType } from '../../modelos/reservation.models';
import { CourtApiService } from '../../servicios/court-api.service';
import { ReservationsApiService } from '../../servicios/reservations-api.service';

@Component({
  selector: 'app-admin-reservas',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-reservas.component.html',
  styleUrl: './admin-reservas.component.css',
})
export class AdminReservasComponent implements OnInit {
  private reservationsApi = inject(ReservationsApiService);
  private courtApi = inject(CourtApiService);

  filterSport = signal<'Todos' | 'FUTBOL' | 'PADEL'>('Todos');
  filterCourtId = signal('TODAS');
  selectedDate = signal(this.today());
  reservations = signal<Reservation[]>([]);
  loading = signal(true);
  filterCourts = signal<CourtResponse[]>([]);
  courtsForBlock = signal<CourtResponse[]>([]);

  blockForm = {
    sport: 'PADEL' as SportType,
    courtId: '',
    date: '',
    time: '',
  };

  ngOnInit() {
    this.loadFilterCourts();
    this.loadReservations();
    this.loadBlockCourts();
  }

  private loadReservations() {
    this.loading.set(true);
    const filters: { sport?: SportType; date?: string } = {
      date: this.selectedDate(),
    };
    if (this.filterSport() !== 'Todos') {
      filters.sport = this.filterSport() as SportType;
    }

    this.reservationsApi.getAll(filters).subscribe({
      next: (reservations) => {
        this.reservations.set(reservations);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  private loadBlockCourts() {
    this.courtApi.getCourts(this.blockForm.sport).subscribe({
      next: (courts) => {
        this.courtsForBlock.set(courts);
        if (!courts.some((court) => court.id === this.blockForm.courtId)) {
          this.blockForm.courtId = courts[0]?.id ?? '';
        }
      },
      error: () => {
        this.courtsForBlock.set([]);
        this.blockForm.courtId = '';
      },
    });
  }

  private loadFilterCourts() {
    let sport: 'FUTBOL' | 'PADEL' | undefined = undefined;
    if (this.filterSport() !== 'Todos') {
      sport = this.filterSport() as 'FUTBOL' | 'PADEL';
    }
    this.courtApi.getCourts(sport).subscribe({
      next: (courts) => {
        this.filterCourts.set(courts);
        if (this.filterSport() === 'Todos') {
          this.filterCourtId.set('TODAS');
        } else if (courts.length > 0) {
          this.filterCourtId.set(courts[0].id);
        } else {
          this.filterCourtId.set('TODAS');
        }
      },
      error: () => {
        this.filterCourts.set([]);
        this.filterCourtId.set('TODAS');
      },
    });
  }

  onBlockSportChange(sport: string) {
    this.blockForm.sport = sport as SportType;
    this.blockForm.courtId = '';
    this.loadBlockCourts();
  }

  applyFilter(sport: 'Todos' | 'FUTBOL' | 'PADEL') {
    this.filterSport.set(sport);
    this.loadFilterCourts();
    this.loadReservations();
  }

  applyCourtFilter(courtId: string) {
    this.filterCourtId.set(courtId);
  }

  changeDate(date: string) {
    this.selectedDate.set(date);
    this.loadReservations();
  }

  dayReservations() {
    return this.visibleReservations();
  }

  scheduleTimes() {
    const baseTimes = ['09:00', '10:30', '12:00', '13:30', '15:00', '16:30', '18:00', '19:30', '21:00'];
    const timeSet = new Set(baseTimes);

    for (const reservation of this.visibleReservations()) {
      timeSet.add(this.normalizeTime(reservation.time));
    }

    return Array.from(timeSet).sort((a, b) => a.localeCompare(b));
  }

  reservationsAt(time: string) {
    return this.visibleReservations().filter((reservation) => this.normalizeTime(reservation.time) === time);
  }

  cancelReservation(id: string) {
    this.reservationsApi.delete(id).subscribe({
      next: () => this.loadReservations(),
    });
  }

  removeMaintenance(id: string) {
    this.cancelReservation(id);
  }

  blockCourt(event: Event) {
    event.preventDefault();
    if (!this.blockForm.date || !this.blockForm.time || !this.blockForm.courtId) return;

    const request: CreateMaintenanceBlockRequest = {
      sport: this.blockForm.sport,
      courtId: this.blockForm.courtId,
      date: this.blockForm.date,
      time: `${this.blockForm.time}:00`,
    };

    this.reservationsApi.createMaintenance(request).subscribe({
      next: () => {
        this.blockForm.date = '';
        this.blockForm.time = '';
        this.loadReservations();
      },
    });
  }

  statusLabel(status: string) {
    if (status === 'PENDING') return 'Pendiente';
    if (status === 'COMPLETED') return 'Completada';
    if (status === 'MAINTENANCE') return 'Mantenimiento';
    return status;
  }

  private visibleReservations() {
    if (this.filterCourtId() === 'TODAS') {
      return this.reservations();
    }

    const selectedCourt = this.filterCourts().find((court) => court.id === this.filterCourtId());
    if (!selectedCourt) {
      return this.reservations();
    }

    return this.reservations().filter((reservation) => reservation.court === selectedCourt.name);
  }

  private normalizeTime(time: string) {
    return time.substring(0, 5);
  }

  private today() {
    const now = new Date();
    return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
  }
}
