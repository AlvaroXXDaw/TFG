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
    { src: 'images/gallery-01.png', label: 'Pista Indoor' },
    { src: 'images/gallery-02.png', label: 'Terraza' },
    { src: 'images/gallery-03.png', label: 'Zona Spa' },
    { src: 'images/gallery-04.png', label: 'Recepción' },
    { src: 'images/gallery-05.png', label: 'Fútbol Nocturno' },
    { src: 'images/gallery-06.png', label: 'Vestuarios' },
    { src: 'images/gallery-07.png', label: 'Sala Yoga' },
    { src: 'images/gallery-08.png', label: 'Fachada' },
    { src: 'images/gallery-09.png', label: 'Cafetería' },
    { src: 'images/gallery-10.png', label: 'Vista Aérea' },
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

    // How far we've scrolled past the top of the wrapper
    const scrolled = -wrapperRect.top;
    const wrapperHeight = wrapper.offsetHeight;
    const viewportWidth = window.innerWidth;
    const trackWidth = track.scrollWidth;

    // Total horizontal distance to travel
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
