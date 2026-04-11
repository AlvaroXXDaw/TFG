import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CourtResponse, CreateCourtRequest} from '../../modelos/court.model';
import {CourtApiService} from '../../servicios/court-api.service';

import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-admin-pistas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-pistas.component.html',
  styleUrls: ['./admin-pistas.component.css']
})
export class AdminPistasComponent implements OnInit {
  private courtService = inject(CourtApiService);

  courts = signal<CourtResponse[]>([]);
  selectedSport = signal<'PADEL' | 'FUTBOL' | null>(null);
  confirmingDeleteId = signal<string | null>(null);

  newCourt: CreateCourtRequest = {
    name: '',
    sport: 'PADEL'
  };

  ngOnInit(): void {
    this.loadCourts();
  }

  loadCourts(): void {
    const sport = this.selectedSport() ?? undefined;
    this.courtService.getCourts(sport).subscribe({
      next: (data) => this.courts.set(data),
      error: (err) => console.error('Error load courts:', err)
    });
  }

  filterBySport(sport: 'PADEL' | 'FUTBOL' | null): void {
    this.selectedSport.set(sport);
    this.confirmingDeleteId.set(null);
    this.loadCourts();
  }

  addCourt(e: Event): void {
    e.preventDefault();
    this.courtService.createCourt(this.newCourt).subscribe({
      next: () => {
        this.loadCourts();
        this.newCourt.name = '';
      },
      error: (err) => console.error('Error create court', err)
    });
  }

  requestDelete(id: string): void {
    this.confirmingDeleteId.set(id);
  }

  cancelDelete(): void {
    this.confirmingDeleteId.set(null);
  }

  confirmDelete(id: string): void {
    this.courtService.deleteCourt(id).subscribe({
      next: () => {
        this.confirmingDeleteId.set(null);
        this.loadCourts();
      },
      error: (err) => console.error('Error delete court', err)
    });
  }
}
