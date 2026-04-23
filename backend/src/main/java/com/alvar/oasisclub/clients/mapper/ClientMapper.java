package com.alvar.oasisclub.clients.mapper;

import com.alvar.oasisclub.clients.dto.ClientResponse;
import com.alvar.oasisclub.clients.dto.CreateClientRequest;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientMapper {

  private final PasswordEncoder passwordEncoder;

  public ClientResponse toResponse(ClientEntity entity) {
    ClientResponse dto = new ClientResponse();
    dto.setId(entity.getId().toString());
    dto.setName(entity.getName());
    dto.setEmail(entity.getEmail());
    dto.setRole(entity.getRole());
    dto.setJoinDate(entity.getJoinDate());
    dto.setPhone(entity.getPhone());
    dto.setBirthDate(entity.getBirthDate());
    return dto;
  }

  public ClientEntity toEntity(CreateClientRequest request) {
    return ClientEntity.builder()
        .name(request.getName().trim())
        .email(request.getEmail().trim().toLowerCase())
        .joinDate(LocalDate.now())
        .passwordHash(passwordEncoder.encode(request.getPassword()))
        .role("MEMBER")
        .phone(request.getPhone().trim())
        .birthDate(request.getBirthDate())
        .build();
  }
}
