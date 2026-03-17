package com.alvar.oasisclub.gym.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.gym.dto.RoutineDayDto;
import com.alvar.oasisclub.gym.dto.UpdateRoutineRequest;
import com.alvar.oasisclub.gym.service.GymService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gym/routines")
@AllArgsConstructor
public class GymController {

  private final GymService gymService;
  private final AccessControlService accessControl;

  @GetMapping("/{clientId}")
  public List<RoutineDayDto> getRoutine(@PathVariable UUID clientId, Authentication authentication) {
    accessControl.requireAdminOrOwner(authentication, clientId);
    return gymService.getRoutine(clientId);
  }

  @PutMapping("/{clientId}")
  public ResponseEntity<List<RoutineDayDto>> updateRoutine(
      @PathVariable UUID clientId,
      @Valid @RequestBody UpdateRoutineRequest request,
      Authentication authentication
  ) {
    accessControl.requireAdminOrOwner(authentication, clientId);
    return ResponseEntity.ok(gymService.updateRoutine(clientId, request));
  }
}

