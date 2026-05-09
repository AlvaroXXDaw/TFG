import { Component, ElementRef, HostListener, ViewChild, AfterViewInit, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css',
})
export class InicioComponent implements AfterViewInit {
  private platformId = inject(PLATFORM_ID);

  @ViewChild('scrollWrapper') scrollWrapper!: ElementRef<HTMLDivElement>;
  @ViewChild('scrollTrack') scrollTrack!: ElementRef<HTMLDivElement>;

  scrollX = 0;

  galleryPhotos = [
    { src: 'images/sport-padel-ultrareal.png', label: 'Pista Indoor' },
    { src: 'images/facility-padel-training-ultrareal.png', label: 'Entrenamiento Padel' },
    { src: 'images/sport-futbol-ultrareal.png', label: 'Futbol Indoor' },
    { src: 'images/facility-futbol-training-ultrareal.png', label: 'Tecnificacion' },
    { src: 'images/sport-gimnasio-ultrareal.png', label: 'Sala Fitness' },
    { src: 'images/facility-strength-ultrareal.png', label: 'Zona Fuerza' },
    { src: 'images/facility-cardio-ultrareal.png', label: 'Cardio Premium' },
    { src: 'images/facility-equipment-ultrareal.png', label: 'Equipamiento' },
    { src: 'images/facility-lounge-ultrareal.png', label: 'Cafeteria' },
    { src: 'images/facility-lockers-ultrareal.png', label: 'Vestuarios' },
  ];

  galleryPairs = this.chunkArray(this.galleryPhotos, 2);

  private chunkArray<T>(arr: T[], size: number): T[][] {
    const result: T[][] = [];
    for (let i = 0; i < arr.length; i += size) {
      result.push(arr.slice(i, i + size));
    }
    return result;
  }

  ngAfterViewInit() {
    this.onScroll();
  }

  @HostListener('window:scroll')
  onScroll() {
    if (!isPlatformBrowser(this.platformId)) return;
    if (!this.scrollWrapper || !this.scrollTrack) return;

    const wrapper = this.scrollWrapper.nativeElement;
    const track = this.scrollTrack.nativeElement;
    const wrapperRect = wrapper.getBoundingClientRect();

    const scrolled = -wrapperRect.top;
    const wrapperHeight = wrapper.offsetHeight;
    const viewportWidth = window.innerWidth;
    const trackWidth = track.scrollWidth;

    const maxScroll = trackWidth - viewportWidth;

    if (scrolled <= 0) {
      this.scrollX = 0;
    } else if (scrolled >= wrapperHeight - window.innerHeight) {
      this.scrollX = -maxScroll;
    } else {
      const progress = scrolled / (wrapperHeight - window.innerHeight);
      this.scrollX = -(progress * maxScroll);
    }
  }
}
