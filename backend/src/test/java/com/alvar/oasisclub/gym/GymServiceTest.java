package com.alvar.oasisclub.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.gym.dto.RoutineDayDto;
import com.alvar.oasisclub.gym.dto.RoutineExerciseDto;
import com.alvar.oasisclub.gym.entity.GymRoutineDayEntity;
import com.alvar.oasisclub.gym.entity.GymRoutineExerciseEntity;
import com.alvar.oasisclub.gym.mapper.GymMapper;
import com.alvar.oasisclub.gym.repository.GymRoutineDayRepository;
import com.alvar.oasisclub.gym.repository.GymRoutineExerciseRepository;
import com.alvar.oasisclub.gym.service.GymService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GymServiceTest {

  @Mock
  private GymRoutineDayRepository dayRepository;

  @Mock
  private GymRoutineExerciseRepository exerciseRepository;

  @Mock
  private GymMapper gymMapper;

  @InjectMocks
  private GymService gymService;

  @Test
  void getRoutineReturnsDaysWithExercises() {
    UUID clientId = UUID.randomUUID();
    UUID dayId = UUID.randomUUID();

    GymRoutineDayEntity dayEntity = GymRoutineDayEntity.builder()
        .id(dayId)
        .clientId(clientId)
        .dayOrder(1)
        .name("Strength")
        .build();

    GymRoutineExerciseEntity exerciseEntity = GymRoutineExerciseEntity.builder()
        .id(UUID.randomUUID())
        .routineDayId(dayId)
        .exerciseOrder(1)
        .name("Squat")
        .setsCount(4)
        .reps("10")
        .restInterval("90s")
        .build();

    RoutineDayDto dayDto = new RoutineDayDto();
    dayDto.setDayOrder(1);
    dayDto.setName("Strength");

    RoutineExerciseDto exerciseDto = new RoutineExerciseDto();
    exerciseDto.setName("Squat");

    when(dayRepository.findByClientIdOrderByDayOrderAsc(clientId)).thenReturn(List.of(dayEntity));
    when(exerciseRepository.findByRoutineDayIdOrderByExerciseOrderAsc(dayId)).thenReturn(List.of(exerciseEntity));
    when(gymMapper.toDayDto(dayEntity)).thenReturn(dayDto);
    when(gymMapper.toExerciseDto(exerciseEntity)).thenReturn(exerciseDto);

    List<RoutineDayDto> result = gymService.getRoutine(clientId);

    assertEquals(1, result.size());
    assertEquals("Strength", result.get(0).getName());
    assertEquals(1, result.get(0).getExercises().size());
    assertEquals("Squat", result.get(0).getExercises().get(0).getName());
  }

  @Test
  void getRoutineReturnsEmptyListWhenNoDays() {
    UUID clientId = UUID.randomUUID();
    when(dayRepository.findByClientIdOrderByDayOrderAsc(clientId)).thenReturn(List.of());

    List<RoutineDayDto> result = gymService.getRoutine(clientId);

    assertEquals(0, result.size());
  }
}

