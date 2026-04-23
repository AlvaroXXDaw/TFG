package com.alvar.oasisclub.clients.service;

import com.alvar.oasisclub.clients.dto.ClientResponse;
import com.alvar.oasisclub.clients.dto.CreateClientRequest;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import com.alvar.oasisclub.clients.exception.ClientEmailAlreadyExistsException;
import com.alvar.oasisclub.clients.exception.ClientNotFoundException;
import com.alvar.oasisclub.clients.mapper.ClientMapper;
import com.alvar.oasisclub.clients.repository.ClientRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ClientService {

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;

  @Transactional(readOnly = true)
  public List<ClientResponse> getAllClients() {
    return clientRepository.findAll().stream()
        .map(clientMapper::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ClientResponse> getClients(String query, Integer limit) {
    int safeLimit = limit == null ? 10 : Math.max(1, Math.min(limit, 50));
    var pageable = PageRequest.of(0, safeLimit);

    boolean hasQuery = query != null && !query.isBlank();
    if (hasQuery) {
      String normalizedQuery = query.trim();
      return clientRepository
          .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByJoinDateDesc(
              normalizedQuery,
              normalizedQuery,
              pageable
          )
          .stream()
          .map(clientMapper::toResponse)
          .toList();
    }

    return clientRepository.findAllByOrderByJoinDateDesc(pageable).stream()
        .map(clientMapper::toResponse)
        .toList();
  }

  @Transactional
  public ClientResponse createClient(CreateClientRequest request) {
    clientRepository.findByEmailIgnoreCase(request.getEmail().trim())
        .ifPresent(c -> {
          throw new ClientEmailAlreadyExistsException("Email already exists");
        });

    if (request.getPhone() != null && !request.getPhone().isBlank()) {
      clientRepository.findByPhone(request.getPhone().trim())
          .ifPresent(c -> {
            throw new ClientEmailAlreadyExistsException("Phone already exists");
          });
    }

    ClientEntity saved = clientRepository.save(clientMapper.toEntity(request));
    return clientMapper.toResponse(saved);
  }

  @Transactional
  public void deleteClient(UUID id) {
    ClientEntity client = getEntityById(id);

    if ("ADMIN".equalsIgnoreCase(client.getRole())) {
      throw new IllegalArgumentException("No se puede eliminar un usuario administrador.");
    }

    clientRepository.delete(client);
  }

  @Transactional(readOnly = true)
  public ClientEntity getEntityById(UUID id) {
    return clientRepository.findById(id)
        .orElseThrow(() -> new ClientNotFoundException("Client not found"));
  }

  @Transactional(readOnly = true)
  public ClientEntity findByEmail(String email) {
    return clientRepository.findByEmailIgnoreCase(email).orElse(null);
  }

  @Transactional(readOnly = true)
  public ClientEntity findByPhone(String phone) {
    return clientRepository.findByPhone(phone).orElse(null);
  }

  @Transactional
  public void save(ClientEntity client) {
    clientRepository.save(client);
  }
}
