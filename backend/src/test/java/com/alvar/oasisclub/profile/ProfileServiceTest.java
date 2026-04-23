package com.alvar.oasisclub.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.service.ClientService;
import com.alvar.oasisclub.profile.dto.ProfileResponse;
import com.alvar.oasisclub.profile.mapper.ProfileMapper;
import com.alvar.oasisclub.profile.service.ProfileService;
import com.alvar.oasisclub.reservations.entity.ReservationEntity;
import com.alvar.oasisclub.reservations.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

  @Mock
  private ClientService clientService;

  @Mock
  private ReservationService reservationService;

  @Mock
  private ProfileMapper profileMapper;

  @InjectMocks
  private ProfileService profileService;

  @Test
  void getProfileReturnsClientData() {
    UUID clientId = UUID.randomUUID();

    ClientEntity client = ClientEntity.builder()
        .id(clientId)
        .name("Alvaro")
        .email("alvaro@test.com")
        .joinDate(LocalDate.now())
        .passwordHash("$2a$10$hashfalso")
        .role("MEMBER")
        .build();

    List<ReservationEntity> reservations = List.of();

    ProfileResponse expectedResponse = new ProfileResponse();
    expectedResponse.setClientId(clientId.toString());
    expectedResponse.setName("Alvaro");
    expectedResponse.setReservations(List.of());

    when(clientService.getEntityById(clientId)).thenReturn(client);
    when(reservationService.getReservationsByClient(clientId)).thenReturn(reservations);
    when(profileMapper.toResponse(client, reservations)).thenReturn(expectedResponse);

    ProfileResponse result = profileService.getProfile(clientId);

    assertNotNull(result);
    assertEquals("Alvaro", result.getName());
    assertEquals(0, result.getReservations().size());
  }
}
