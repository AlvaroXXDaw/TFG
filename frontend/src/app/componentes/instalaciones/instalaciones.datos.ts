export interface FacilityData {
  title: string;
  subtitle: string;
  heroImage: string;
  description: string;
  features: string[];
  ctaText: string;
  ctaRoute: string;
  detailImages: { src: string; alt: string }[];
}

export const FACILITIES: Record<string, FacilityData> = {
  padel: {
    title: 'Pádel',
    subtitle: 'El estándar profesional, a tu alcance.',
    heroImage: 'images/padel.png',
    description: 'Nuestras instalaciones cuentan con 8 pistas panorámicas de cristal de última generación. Diseñadas para ofrecer la máxima visibilidad y un rebote perfecto, cumpliendo con las normativas del World Padel Tour.',
    features: [
      'Césped Mondo Supercourt XN',
      'Iluminación LED anti-deslumbramiento (1000 lux)',
      'Techos de 12 metros de altura libre',
      'Climatización inteligente',
    ],
    ctaText: 'Reservar Pista',
    ctaRoute: '/reservar',
    detailImages: [
      { src: 'images/detalle-padel.png', alt: 'Detalle Pádel' },
      { src: 'images/vestuarios.png', alt: 'Vestuarios' },
    ],
  },
  futbol: {
    title: 'Fútbol',
    subtitle: 'El terreno de juego perfecto.',
    heroImage: 'images/futbol.png',
    description: 'Disfruta de 4 campos de Fútbol 7 indoor diseñados para ofrecer la experiencia definitiva. Sin importar el clima exterior, nuestro césped de calidad FIFA Pro garantiza un bote regular y previene lesiones.',
    features: [
      'Césped artificial de última generación (FIFA Quality Pro)',
      'Marcadores electrónicos y grabación de partidos',
      'Balones premium incluidos en la reserva',
      'Gradas minimalistas para espectadores',
    ],
    ctaText: 'Reservar Campo',
    ctaRoute: '/reservar',
    detailImages: [
      { src: 'images/detalle-futbol.png', alt: 'Detalle Fútbol' },
      { src: 'images/gradas.png', alt: 'Gradas' },
    ],
  },
  gimnasio: {
    title: 'Gimnasio',
    subtitle: 'Máximo rendimiento. Cero distracciones.',
    heroImage: 'images/gimnasio.png',
    description: 'Un espacio de 1.500m² dedicado al culto del cuerpo y la mente. Equipado íntegramente con maquinaria de alta gama, zonas de peso libre expansivas y un ambiente acústico y lumínico diseñado para el enfoque total.',
    features: [
      'Maquinaria conectada Technogym Artis',
      'Zona de peso libre con equipamiento Eleiko',
      'Área de recuperación con crioterapia y sauna',
      'Iluminación circadiana para optimizar la energía',
    ],
    ctaText: 'Ver mi rutina',
    ctaRoute: '/gimnasio',
    detailImages: [
      { src: 'images/detalle-gimnasio.png', alt: 'Detalle Gimnasio' },
      { src: 'images/zona-musculacion.png', alt: 'Zona Cardio' },
    ],
  },
};
