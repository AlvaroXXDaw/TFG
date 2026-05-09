export interface FeatureCard {
  image: string;
  title: string;
  subtitle: string;
}

export interface BenefitItem {
  icon: string;
  title: string;
}

export interface FacilityData {
  sportLabel: string;
  heroTitle: string;
  heroAccentText: string;
  heroDescription: string;
  heroImage: string;
  primaryButtonText: string;
  primaryButtonRoute: string;
  secondaryButtonText: string;
  secondaryButtonRoute: string;
  
  featureCards: FeatureCard[];
  
  experienceTitle: string;
  experienceDescription: string;
  
  facilitiesList: string[];
  classesList: string[];
  includedList: string[];
  
  benefitItems: BenefitItem[];
  
  bottomEyebrow: string;
  bottomTitle: string;
  bottomDescription: string;
  bottomPrimaryButtonText: string;
  bottomPrimaryButtonRoute: string;
  bottomSecondaryButtonText: string;
  bottomSecondaryButtonRoute: string;
  bottomImage: string;
}

export const FACILITIES: Record<string, FacilityData> = {
  futbol: {
    sportLabel: 'FÚTBOL',
    heroTitle: 'Fútbol',
    heroAccentText: 'de alto nivel.',
    heroDescription: 'Instalaciones profesionales, entrenamientos de calidad y un entorno diseñado para competir, mejorar y disfrutar del juego.',
    heroImage: 'images/sport-futbol-ultrareal.png',
    primaryButtonText: 'Reservar pista',
    primaryButtonRoute: '/reservar',
    secondaryButtonText: 'Ver tarifas',
    secondaryButtonRoute: '/tarifas',

    featureCards: [
      { image: 'images/sport-futbol-ultrareal.png', title: 'Campo indoor', subtitle: 'Césped de calidad profesional' },
      { image: 'images/facility-futbol-training-ultrareal.png', title: 'Entrenamiento técnico', subtitle: 'Sesiones guiadas para perfeccionar' },
      { image: 'images/facility-equipment-ultrareal.png', title: 'Equipamiento top', subtitle: 'Balones y material siempre disponibles' }
    ],

    experienceTitle: 'Mucho más que fútbol.',
    experienceDescription: 'Vivimos el fútbol como una experiencia completa. Entrena, compite y disfruta en un entorno premium con todo lo que necesitas para dar tu mejor versión.',

    facilitiesList: [
      'Campo indoor profesional',
      'Iluminación LED',
      'Gradas laterales',
      'Vestuario premium',
      'Parking privado'
    ],
    classesList: [
      'Escuela para jóvenes',
      'Entrenamiento personal',
      'Tecnificación',
      'Partidos y ligas',
      'Todos los niveles'
    ],
    includedList: [
      'Uso de instalaciones',
      'Acceso a vestuarios',
      'Agua y toalla',
      'Descuentos en eventos',
      'Invitación a torneos internos'
    ],

    benefitItems: [
      { icon: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z', title: 'Reserva online' },
      { icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0', title: 'Comunidad activa' },
      { icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z', title: 'Entrenadores expertos' },
      { icon: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4', title: 'Entorno exclusivo' },
      { icon: 'M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z', title: 'Competiciones' }
    ],

    bottomEyebrow: 'LISTO PARA TU MEJOR VERSIÓN',
    bottomTitle: 'Reserva tu partido o hazte socio.',
    bottomDescription: 'Disfruta del fútbol en OASISCLUB. Entrena, compite y forma parte del club.',
    bottomPrimaryButtonText: 'Reservar pista',
    bottomPrimaryButtonRoute: '/reservar',
    bottomSecondaryButtonText: 'Hazte socio',
    bottomSecondaryButtonRoute: '/registro',
    bottomImage: 'images/sport-futbol-ultrareal.png'
  },
  padel: {
    sportLabel: 'PÁDEL',
    heroTitle: 'Pádel',
    heroAccentText: 'de alto nivel.',
    heroDescription: 'Instalaciones profesionales, tecnología de última generación y un entorno diseñado para que lleves tu juego al siguiente nivel.',
    heroImage: 'images/sport-padel-ultrareal.png',
    primaryButtonText: 'Reservar pista',
    primaryButtonRoute: '/reservar',
    secondaryButtonText: 'Ver tarifas',
    secondaryButtonRoute: '/tarifas',

    featureCards: [
      { image: 'images/sport-padel-ultrareal.png', title: 'Instalaciones premium', subtitle: 'Pistas indoor con iluminación LED' },
      { image: 'images/facility-padel-training-ultrareal.png', title: 'Entrenamiento de élite', subtitle: 'Programas diseñados para todos' },
      { image: 'images/facility-equipment-ultrareal.png', title: 'Equipamiento top', subtitle: 'Material de las mejores marcas' }
    ],

    experienceTitle: 'Mucho más que pádel.',
    experienceDescription: 'En OASISCLUB encontrarás todo lo que necesitas para disfrutar, mejorar y superarte cada día en un ambiente premium.',

    facilitiesList: [
      '4 pistas indoor profesionales',
      'Cristal panorámico',
      'Iluminación LED',
      'Vestuario premium',
      'Parking privado'
    ],
    classesList: [
      'Clases particulares',
      'Todos los niveles',
      'Metodología progresiva',
      'Academia junior',
      'Torneos internos'
    ],
    includedList: [
      'Uso de pistas',
      'Acceso a lounge',
      'Agua y toalla',
      'Descuentos en tienda',
      'Eventos del club'
    ],

    benefitItems: [
      { icon: 'M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z', title: 'Reserva online' },
      { icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0', title: 'Comunidad activa' },
      { icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z', title: 'Entrenadores expertos' },
      { icon: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4', title: 'Entorno exclusivo' },
    ],

    bottomEyebrow: 'LISTO PARA TU MEJOR VERSIÓN',
    bottomTitle: 'Reserva tu pista o hazte socio.',
    bottomDescription: 'Disfruta de ventajas exclusivas y forma parte de una comunidad que vive el deporte cada día.',
    bottomPrimaryButtonText: 'Reservar pista',
    bottomPrimaryButtonRoute: '/reservar',
    bottomSecondaryButtonText: 'Hazte socio',
    bottomSecondaryButtonRoute: '/registro',
    bottomImage: 'images/facility-lounge-ultrareal.png'
  },
  gimnasio: {
    sportLabel: 'SALA DE MÁQUINAS',
    heroTitle: 'Sala de máquinas',
    heroAccentText: 'de alto nivel.',
    heroDescription: 'Equipamiento de última generación, espacios amplios y diseño pensado para que entrenes al máximo, con el confort que mereces.',
    heroImage: 'images/sport-gimnasio-ultrareal.png',
    primaryButtonText: 'Conocer más',
    primaryButtonRoute: '/instalaciones/gimnasio',
    secondaryButtonText: 'Hazte socio',
    secondaryButtonRoute: '/registro',

    featureCards: [
      { image: 'images/facility-strength-ultrareal.png', title: 'Peso libre', subtitle: 'Zona de fuerza de alto rendimiento' },
      { image: 'images/sport-gimnasio-ultrareal.png', title: 'Máquinas guiadas', subtitle: 'Equipamiento de última generación' },
      { image: 'images/facility-cardio-ultrareal.png', title: 'Cardio premium', subtitle: 'Cintas y bicis con conectividad' }
    ],

    experienceTitle: 'Mucho más que entrenar.',
    experienceDescription: 'Diseñamos cada detalle para que vivas una experiencia completa, motivadora y alineada con tu bienestar.',

    facilitiesList: [
      'Zona de fuerza',
      'Máquinas guiadas',
      'Área de cardio premium',
      'Vestuarios y duchas',
      'Toalla incluida'
    ],
    classesList: [
      'Asesoramiento',
      'Entrenadores certificados',
      'Clases dirigidas',
      'Valoraciones físicas',
      'App OASISCLUB'
    ],
    includedList: [
      'Acceso a sala',
      'Clases dirigidas',
      'Toalla y agua',
      'Taquilla',
      'Wi-Fi de alta velocidad'
    ],

    benefitItems: [
      { icon: 'M9 3H5a2 2 0 00-2 2v4m6-6h10a2 2 0 012 2v4M9 3v18m0 0h10a2 2 0 002-2V9M9 21H5a2 2 0 01-2-2V9m0 0h18', title: 'Equipamiento premium' },
      { icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z', title: 'Entrenadores expertos' },
      { icon: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4', title: 'Ambiente exclusivo' },
      { icon: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0', title: 'Comunidad activa' },
    ],

    bottomEyebrow: 'LISTO PARA TU MEJOR VERSIÓN',
    bottomTitle: 'Reserva tu sesión o hazte socio.',
    bottomDescription: 'Descubre todo lo que OASISCLUB puede hacer por tu rendimiento y tu bienestar.',
    bottomPrimaryButtonText: 'Reservar sesión',
    bottomPrimaryButtonRoute: '/gimnasio',
    bottomSecondaryButtonText: 'Hazte socio',
    bottomSecondaryButtonRoute: '/registro',
    bottomImage: 'images/facility-lockers-ultrareal.png'
  }
};
