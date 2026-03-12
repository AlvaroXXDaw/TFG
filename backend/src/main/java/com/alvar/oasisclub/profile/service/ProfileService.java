package com.alvar.oasisclub.profile.service;

import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.profile.dto.ProfileResponse;
import com.alvar.oasisclub.profile.mapper.ProfileMapper;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.service.ReservationService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProfileService {

  private final ClientService clientService;
  private final ReservationService reservationService;
  private final ProfileMapper profileMapper;

  @Transactional(readOnly = true)
  public ProfileResponse getProfile(UUID clientId) {
    ClientEntity client = clientService.getEntityById(clientId);
    List<ReservationEntity> reservations = reservationService.getReservationsByClient(clientId);
    return profileMapper.toResponse(client, reservations);
  }
}


