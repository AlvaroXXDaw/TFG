package com.alvar.oasisclub.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alvar.oasisclub.clients.dto.ClientResponse;
import com.alvar.oasisclub.clients.dto.CreateClientRequest;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.entity.ClientPlan;
import com.alvar.oasisclub.clients.exception.ClientEmailAlreadyExistsException;
import com.alvar.oasisclub.clients.exception.ClientNotFoundException;
import com.alvar.oasisclub.clients.mapper.ClientMapper;
import com.alvar.oasisclub.clients.repository.ClientRepository;
import com.alvar.oasisclub.clients.service.ClientService;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

  @Mock
  private ClientRepository clientRepository;

  @Mock
  private ClientMapper clientMapper;

  @InjectMocks
  private ClientService clientService;

  private CreateClientRequest createRequest() {
    CreateClientRequest request = new CreateClientRequest();
    request.setName("Carlos");
    request.setEmail("carlos@test.com");
    request.setPlan(ClientPlan.BASIC);
    return request;
  }

  private ClientEntity createEntity() {
    return ClientEntity.builder()
        .id(UUID.randomUUID())
        .name("Carlos")
        .email("carlos@test.com")
        .plan(ClientPlan.BASIC)
        .joinDate(LocalDate.now())
        .subscriptionName("Basic Monthly")
        .nextBillingDate(LocalDate.now().plusMonths(1))
        .subscriptionAmountCents(3999)
        .passwordHash("hash")
        .role("MEMBER")
        .build();
  }

  @Test
  void createOk() {
    CreateClientRequest request = createRequest();
    ClientEntity entity = createEntity();
    ClientResponse response = new ClientResponse();
    response.setName("Carlos");

    when(clientRepository.findByEmailIgnoreCase("carlos@test.com")).thenReturn(Optional.empty());
    when(clientMapper.toEntity(request)).thenReturn(entity);
    when(clientRepository.save(entity)).thenReturn(entity);
    when(clientMapper.toResponse(entity)).thenReturn(response);

    ClientResponse result = clientService.createClient(request);

    assertEquals("Carlos", result.getName());
    verify(clientRepository).save(entity);
  }

  @Test
  void createDuplicateEmailThrows() {
    CreateClientRequest request = createRequest();

    when(clientRepository.findByEmailIgnoreCase("carlos@test.com"))
        .thenReturn(Optional.of(createEntity()));

    assertThrows(ClientEmailAlreadyExistsException.class, () -> clientService.createClient(request));
  }

  @Test
  void deleteMissingThrows() {
    UUID id = UUID.randomUUID();
    when(clientRepository.existsById(id)).thenReturn(false);

    assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(id));
  }

  @Test
  void deleteOk() {
    UUID id = UUID.randomUUID();
    when(clientRepository.existsById(id)).thenReturn(true);

    clientService.deleteClient(id);

    verify(clientRepository).deleteById(id);
  }
}


