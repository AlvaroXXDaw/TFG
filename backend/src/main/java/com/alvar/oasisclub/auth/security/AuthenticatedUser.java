package com.alvar.oasisclub.auth.security;

import java.util.UUID;

public record AuthenticatedUser(UUID clientId, String email, String role) {
}

