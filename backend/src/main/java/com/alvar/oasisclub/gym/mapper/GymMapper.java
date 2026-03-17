package com.alvar.oasisclub.gym.mapper;

import com.alvar.oasisclub.gym.dto.RoutineDayDto;
import com.alvar.oasisclub.gym.dto.RoutineExerciseDto;
import com.alvar.oasisclub.gym.entity.GymRoutineDayEntity;
import com.alvar.oasisclub.gym.entity.GymRoutineExerciseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GymMapper {

  @Mapping(target = "id", expression = "java(dayEntity.getId().toString())")
  @Mapping(target = "exercises", ignore = true)
  RoutineDayDto toDayDto(GymRoutineDayEntity dayEntity);

  @Mapping(target = "id", expression = "java(entity.getId().toString())")
  @Mapping(target = "order", source = "exerciseOrder")
  @Mapping(target = "sets", source = "setsCount")
  @Mapping(target = "rest", source = "restInterval")
  RoutineExerciseDto toExerciseDto(GymRoutineExerciseEntity entity);
}

