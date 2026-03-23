import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { FACILITIES, FacilityData } from './instalaciones.datos';

@Component({
  selector: 'app-instalaciones',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './instalaciones.component.html',
  styleUrl: './instalaciones.component.css',
})
export class InstalacionesComponent implements OnInit {
  private route = inject(ActivatedRoute);

  facility = signal<FacilityData | null>(null);

  ngOnInit() {
    const facilityId = this.route.snapshot.paramMap.get('facilityId');
    if (facilityId && FACILITIES[facilityId]) {
      this.facility.set(FACILITIES[facilityId]);
    }
  }
}
