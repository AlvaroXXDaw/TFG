package com.alvar.oasisclub.auth.mapper;

import com.alvar.oasisclub.auth.dto.AuthSessionResponse;
import com.alvar.oasisclub.clients.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  @Mapping(target = "clientId", expression = "java(client.getId().toString())")
  AuthSessionResponse toResponse(ClientEntity client, String token);
}
