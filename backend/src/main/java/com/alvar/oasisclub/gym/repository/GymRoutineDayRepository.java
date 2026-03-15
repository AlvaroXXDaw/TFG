package com.alvar.oasisclub.gym.repository;

import com.alvar.oasisclub.gym.entity.GymRoutineDayEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRoutineDayRepository extends JpaRepository<GymRoutineDayEntity, UUID> {
  List<GymRoutineDayEntity> findByClientIdOrderByDayOrderAsc(UUID clientId);
  void deleteByClientId(UUID clientId);
}


