package com.alvar.oasisclub.clients.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.clients.dto.ClientResponse;
import com.alvar.oasisclub.clients.dto.CreateClientRequest;
import com.alvar.oasisclub.clients.service.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
public class ClientController {

  private final ClientService clientService;
  private final AccessControlService accessControl;

  @GetMapping
  public List<ClientResponse> getClients(
      @RequestParam(required = false) String query,
      @RequestParam(required = false) Integer limit,
      Authentication authentication
  ) {
    accessControl.requireAdmin(authentication);
    return clientService.getClients(query, limit);
  }

  @PostMapping
  public ResponseEntity<ClientResponse> createClient(
      @Valid @RequestBody CreateClientRequest request,
      Authentication authentication
  ) {
    accessControl.requireAdmin(authentication);
    return ResponseEntity.ok(clientService.createClient(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteClient(@PathVariable UUID id, Authentication authentication) {
    accessControl.requireAdmin(authentication);
    clientService.deleteClient(id);
    return ResponseEntity.noContent().build();
  }
}
