package com.alvar.oasisclub.courts.repository;

import com.alvar.oasisclub.courts.entity.CourtEntity;
import com.alvar.oasisclub.reservations.entity.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourtRepository extends JpaRepository<CourtEntity, UUID> {
    List<CourtEntity> findByIsActiveOrderByCreatedAtAsc(Boolean isActive);
    List<CourtEntity> findBySportAndIsActiveOrderByCreatedAtAsc(SportType sport, Boolean isActive);
    List<CourtEntity> findBySportOrderByCreatedAtAsc(SportType sport);
}

