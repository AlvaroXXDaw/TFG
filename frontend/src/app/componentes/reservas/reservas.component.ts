import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthStore } from '../../auth.store';
import { CourtResponse } from '../../modelos/court.model';
import { AvailabilitySlot } from '../../modelos/reservation.models';
import { CourtApiService } from '../../servicios/court-api.service';
import { PaymentsApiService } from '../../servicios/payments-api.service';
import { ReservationsApiService } from '../../servicios/reservations-api.service';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reservas.component.html',
  styleUrls: ['./reservas.component.css'],
})
export class ReservasComponent implements OnInit {
  auth = inject(AuthStore);

  private reservationsService = inject(ReservationsApiService);
  private courtService = inject(CourtApiService);
  private paymentsService = inject(PaymentsApiService);

  currentStep = signal<number>(1);
  selectedSport = signal<'PADEL' | 'FUTBOL' | null>(null);
  selectedCourt = signal<CourtResponse | null>(null);
  selectedDate = signal<Date>(new Date());
  availableDates = signal<Date[]>([]);
  availableCourts = signal<CourtResponse[]>([]);
  availableTimes = signal<AvailabilitySlot[]>([]);
  selectedTime = signal<string | null>(null);

  isSubmitting = signal<boolean>(false);
  successMessage = signal<string>('');
  errorMessage = signal<string>('');

  ngOnInit() {
    this.generateAvailableDates();
  }

  private generateAvailableDates() {
    const dates: Date[] = [];
    const today = new Date();
    for (let i = 0; i < 7; i++) {
      const d = new Date(today);
      d.setDate(today.getDate() + i);
      dates.push(d);
    }
    this.availableDates.set(dates);
  }

  selectSport(sport: 'PADEL' | 'FUTBOL') {
    this.selectedSport.set(sport);
    this.selectedCourt.set(null);
    this.selectedTime.set(null);
    this.availableTimes.set([]);
    this.errorMessage.set('');
    this.loadCourtsForSport(sport);
    this.currentStep.set(2);
  }

  private loadCourtsForSport(sport: 'PADEL' | 'FUTBOL') {
    this.courtService.getCourts(sport).subscribe({
      next: (courts) => this.availableCourts.set(courts),
      error: () => this.errorMessage.set('Error cargando las pistas'),
    });
  }

  selectCourt(court: CourtResponse) {
    this.selectedCourt.set(court);
    this.selectedTime.set(null);
    this.loadAvailability();
    this.currentStep.set(3);
  }

  selectDate(d: Date) {
    this.selectedDate.set(d);
    this.selectedTime.set(null);
    if (this.selectedCourt()) {
      this.loadAvailability();
    }
  }

  private loadAvailability() {
    const court = this.selectedCourt();
    if (!court) return;

    this.errorMessage.set('');
    const dateStr = this.formatDate(this.selectedDate());

    this.reservationsService.getAvailability(court.id, dateStr).subscribe({
      next: (slots) => {
        const sortedSlots = [...slots].sort((a, b) => a.time.localeCompare(b.time));
        this.availableTimes.set(sortedSlots);
      },
      error: (err: unknown) => {
        console.error('Error getting availability:', err);
        this.errorMessage.set('No se pudo cargar la disponibilidad.');
      },
    });
  }

  selectTime(time: string, isAvailable: boolean) {
    if (isAvailable) {
      this.selectedTime.set(time);
      this.currentStep.set(4);
    }
  }

  confirmReservation() {
    const time = this.selectedTime();
    const court = this.selectedCourt();
    const sport = this.selectedSport();
    const session = this.auth.session();

    if (!time || !court || !sport) return;
    if (!session) {
      this.errorMessage.set('Debes iniciar sesión para reservar.');
      return;
    }

    this.isSubmitting.set(true);
    this.errorMessage.set('');

    const request = {
      sport,
      courtId: court.id,
      date: this.formatDate(this.selectedDate()),
      time: `${time}:00`,
    };

    this.paymentsService.createCheckoutSession(request).subscribe({
      next: (response) => {
        window.location.assign(response.checkoutUrl);
      },
      error: (err: unknown) => {
        console.error('Reservation error:', err);
        this.isSubmitting.set(false);
        this.errorMessage.set(this.paymentErrorMessage(err));
      },
    });
  }

  private paymentErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 0) {
        return 'No se pudo conectar con el backend.';
      }
      if (typeof error.error?.message === 'string' && error.error.message.trim()) {
        return error.error.message;
      }
    }
    return 'Error al procesar la reserva. Intentalo de nuevo.';
  }

  reservationPrice(): string {
    const sport = this.selectedSport();
    if (sport === 'FUTBOL') return '100 EUR';
    if (sport === 'PADEL') return '50 EUR';
    return '';
  }

  private formatDate(d: Date): string {
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  formatDateUI(d: Date): string {
    const days = ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'];
    return `${days[d.getDay()]} ${d.getDate()}`;
  }

  isSelectedDate(day: Date): boolean {
    return this.formatDate(this.selectedDate()) === this.formatDate(day);
  }

  goBack() {
    const current = this.currentStep();
    if (current > 1 && current < 5) {
      this.currentStep.set(current - 1);
      this.errorMessage.set('');
    }
  }

  reset() {
    this.currentStep.set(1);
    this.selectedSport.set(null);
    this.selectedCourt.set(null);
    this.selectedTime.set(null);
    this.selectedDate.set(new Date());
    this.availableCourts.set([]);
    this.availableTimes.set([]);
    this.successMessage.set('');
    this.errorMessage.set('');
    this.generateAvailableDates();
  }
}
