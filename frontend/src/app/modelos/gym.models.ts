export interface RoutineExercise {
  id?: string;
  tempKey?: string;
  order?: number;
  name: string;
  sets: number;
  reps: string;
  rest: string;
}

export interface RoutineDay {
  id?: string;
  dayOrder: number;
  name: string;
  exercises: RoutineExercise[];
}

export interface UpdateRoutineRequest {
  days: RoutineDay[];
}
