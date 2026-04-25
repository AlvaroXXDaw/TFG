package com.alvar.oasisclub.schedule.repository;

import com.alvar.oasisclub.schedule.entity.ScheduleSlotEntity;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlotEntity, UUID> {

  List<ScheduleSlotEntity> findAllByOrderBySlotTimeAsc();

  boolean existsBySlotTime(LocalTime slotTime);

  void deleteBySlotTime(LocalTime slotTime);
}
