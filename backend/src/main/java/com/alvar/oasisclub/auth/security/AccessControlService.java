package com.alvar.oasisclub.auth.security;

import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

  public AuthenticatedUser requireUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("Authentication required");
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof AuthenticatedUser user)) {
      throw new AccessDeniedException("Invalid authentication context");
    }

    return user;
  }

  public boolean isAdmin(Authentication authentication) {
    return authentication != null
        && authentication.getAuthorities().stream()
        .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
  }

  public void requireAdmin(Authentication authentication) {
    if (!isAdmin(authentication)) {
      throw new AccessDeniedException("Admin role required");
    }
  }

  public void requireAdminOrOwner(Authentication authentication, UUID ownerId) {
    if (isAdmin(authentication)) {
      return;
    }

    AuthenticatedUser user = requireUser(authentication);
    if (!user.clientId().equals(ownerId)) {
      throw new AccessDeniedException("You do not have access to this resource");
    }
  }
}

