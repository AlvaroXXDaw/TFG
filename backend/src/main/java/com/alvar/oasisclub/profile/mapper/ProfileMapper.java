package com.alvar.oasisclub.profile.mapper;

import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.profile.dto.ProfileReservationResponse;
import com.alvar.oasisclub.profile.dto.ProfileResponse;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

  public ProfileResponse toResponse(ClientEntity client, List<ReservationEntity> reservations) {
    ProfileResponse response = new ProfileResponse();
    response.setClientId(client.getId().toString());
    response.setName(client.getName());
    response.setReservations(reservations.stream().map(this::toReservationResponse).toList());
    return response;
  }

  private ProfileReservationResponse toReservationResponse(ReservationEntity entity) {
    ProfileReservationResponse dto = new ProfileReservationResponse();
    dto.setId(entity.getId().toString());
    dto.setSport(entity.getSport().name());
    dto.setCourt(entity.getCourt().getName());
    dto.setDate(entity.getReservationDate());
    dto.setTime(entity.getReservationTime());
    dto.setStatus(entity.getStatus().name());
    return dto;
  }
}
