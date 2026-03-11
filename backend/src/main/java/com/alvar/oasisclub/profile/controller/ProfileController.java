package com.alvar.oasisclub.profile.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.profile.dto.ProfileResponse;
import com.alvar.oasisclub.profile.service.ProfileService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class ProfileController {

  private final ProfileService profileService;
  private final AccessControlService accessControl;

  @GetMapping("/{clientId}")
  public ProfileResponse getProfile(@PathVariable UUID clientId, Authentication authentication) {
    accessControl.requireAdminOrOwner(authentication, clientId);
    return profileService.getProfile(clientId);
  }
}

