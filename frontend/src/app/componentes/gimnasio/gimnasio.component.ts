import {Component, computed, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';

import {AuthStore} from '../../auth.store';
import {RoutineDay, RoutineExercise} from '../../modelos/gym.models';
import {GymApiService} from '../../servicios/gym-api.service';

type EditableExerciseField = 'name' | 'sets' | 'reps' | 'rest';

@Component({
  selector: 'app-gimnasio',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './gimnasio.component.html',
  styleUrl: './gimnasio.component.css',
})
export class GimnasioComponent implements OnInit {
  auth = inject(AuthStore);
  private authStore = inject(AuthStore);
  private gymApi = inject(GymApiService);
  private tempExerciseCounter = 0;

  isEditing = signal(false);
  daysCount = signal(1);
  selectedDayId = signal(1);
  routineDays = signal<RoutineDay[]>([]);

  currentDay = computed(() => this.routineDays().find((d) => d.dayOrder === this.selectedDayId()));

  ngOnInit() {
    const session = this.authStore.session();
    if (!session) {
      const defaultRoutine = this.ensureExercisesHaveKeys(this.defaultRoutine());
      this.routineDays.set(defaultRoutine);
      this.daysCount.set(defaultRoutine.length);
      this.selectedDayId.set(defaultRoutine[0]?.dayOrder ?? 1);
      return;
    }

    this.gymApi.getByClient(session.clientId).subscribe({
      next: (days) => {
        const routineToUse = days.length > 0 ? days : this.defaultRoutine();
        const routineWithKeys = this.ensureExercisesHaveKeys(routineToUse);
        this.routineDays.set(routineWithKeys);
        this.daysCount.set(routineWithKeys.length);
        this.selectedDayId.set(routineWithKeys[0]?.dayOrder ?? 1);
      },
    });
  }

  toggleEdit() {
    const nextState = !this.isEditing();
    this.isEditing.set(nextState);

    if (!nextState) {
      this.persistRoutine();
    }
  }

  updateDays(change: number) {
    const newCount = this.daysCount() + change;
    if (newCount < 1 || newCount > 7) {
      return;
    }

    this.routineDays.update((days) => {
      if (change > 0) {
        const maxOrder = days.length > 0 ? Math.max(...days.map((d) => d.dayOrder ?? 0)) : 0;
        return [...days, {dayOrder: maxOrder + 1, name: '', exercises: []}];
      }

      const updated = days.slice(0, newCount);
      if (!updated.some((d) => d.dayOrder === this.selectedDayId())) {
        this.selectedDayId.set(updated[updated.length - 1]?.dayOrder ?? 1);
      }
      return updated;
    });

    this.daysCount.set(newCount);
    this.persistRoutine();
  }

  removeCurrentDay() {
    const idToRemove = this.selectedDayId();

    this.routineDays.update((days) => {
      if (days.length <= 1) {
        return days;
      }

      const filtered = days.filter((d) => d.dayOrder !== idToRemove);
      const reindexed = filtered.map((day, index) => ({...day, dayOrder: index + 1}));
      this.selectedDayId.set(reindexed[0]?.dayOrder ?? 1);
      this.daysCount.set(reindexed.length);
      return reindexed;
    });

    this.persistRoutine();
  }

  selectDay(dayOrder: number) {
    this.selectedDayId.set(dayOrder);
  }

  updateDayName(name: string) {
    this.routineDays.update((days) =>
      days.map((d) => (d.dayOrder === this.selectedDayId() ? {...d, name} : d)),
    );
    this.persistRoutine();
  }

  addExercise() {
    this.routineDays.update((days) =>
      days.map((d) => {
        if (d.dayOrder === this.selectedDayId()) {
          const newExercise: RoutineExercise = {
            tempKey: this.nextTempExerciseKey(),
            name: '',
            sets: 3,
            reps: '10',
            rest: '60s',
          };
          return {...d, exercises: [...d.exercises, newExercise]};
        }
        return d;
      }),
    );
    this.persistRoutine();
  }

  getExerciseTrackKey(exercise: RoutineExercise, index: number): string {
    return exercise.id ?? exercise.tempKey ?? `exercise-${index}`;
  }

  updateExercise(exerciseKey: string, field: EditableExerciseField, value: string | number) {
    if (!exerciseKey) {
      return;
    }

    this.routineDays.update((days) =>
      days.map((d) => {
        if (d.dayOrder === this.selectedDayId()) {
          return {
            ...d,
            exercises: d.exercises.map((e) =>
              this.getExerciseKey(e) === exerciseKey ? {...e, [field]: value} : e,
            ),
          };
        }
        return d;
      }),
    );
    this.persistRoutine();
  }

  removeExercise(exerciseKey: string) {
    if (!exerciseKey) {
      return;
    }

    this.routineDays.update((days) =>
      days.map((d) => {
        if (d.dayOrder === this.selectedDayId()) {
          return {...d, exercises: d.exercises.filter((e) => this.getExerciseKey(e) !== exerciseKey)};
        }
        return d;
      }),
    );
    this.persistRoutine();
  }

  private persistRoutine() {
    const session = this.authStore.session();
    if (!session) {
      return;
    }

    const payloadDays = this.routineDays().map((day) => ({
      ...day,
      exercises: day.exercises.map((exercise) => ({
        id: exercise.id,
        order: exercise.order,
        name: exercise.name,
        sets: exercise.sets,
        reps: exercise.reps,
        rest: exercise.rest,
      })),
    }));

    this.gymApi.update(session.clientId, {days: payloadDays}).subscribe();
  }

  private ensureExercisesHaveKeys(days: RoutineDay[]): RoutineDay[] {
    return days.map((day) => ({
      ...day,
      exercises: day.exercises.map((exercise) => this.ensureExerciseKey(exercise)),
    }));
  }

  private ensureExerciseKey(exercise: RoutineExercise): RoutineExercise {
    if (exercise.id || exercise.tempKey) {
      return exercise;
    }

    return {
      ...exercise,
      tempKey: this.nextTempExerciseKey(),
    };
  }

  private getExerciseKey(exercise: RoutineExercise): string | undefined {
    return exercise.id ?? exercise.tempKey;
  }

  private nextTempExerciseKey(): string {
    this.tempExerciseCounter += 1;
    return `temp-${this.tempExerciseCounter}`;
  }

  private defaultRoutine(): RoutineDay[] {
    return [
      {
        dayOrder: 1,
        name: 'Fuerza',
        exercises: [
          {name: 'Sentadilla Libre', sets: 4, reps: '10', rest: '90s'},
          {name: 'Press de Banca', sets: 4, reps: '8', rest: '90s'},
          {name: 'Dominadas', sets: 3, reps: '10', rest: '60s'},
          {name: 'Plancha Abdominal', sets: 3, reps: '60s', rest: '45s'},
        ],
      },
      {dayOrder: 2, name: '', exercises: []},
      {dayOrder: 3, name: '', exercises: []},
    ];
  }
}
