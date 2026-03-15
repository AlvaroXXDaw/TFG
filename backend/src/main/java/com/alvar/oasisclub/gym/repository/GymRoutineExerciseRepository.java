package com.alvar.oasisclub.gym.repository;

import com.alvar.oasisclub.gym.entity.GymRoutineExerciseEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRoutineExerciseRepository extends JpaRepository<GymRoutineExerciseEntity, UUID> {
  List<GymRoutineExerciseEntity> findByRoutineDayIdOrderByExerciseOrderAsc(UUID routineDayId);
  void deleteByRoutineDayIdIn(List<UUID> routineDayIds);
}


