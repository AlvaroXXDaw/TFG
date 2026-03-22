package com.alvar.oasisclub.courts.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.courts.dto.CourtResponse;
import com.alvar.oasisclub.courts.dto.CreateCourtRequest;
import com.alvar.oasisclub.courts.service.CourtService;
import com.alvar.oasisclub.reservations.entity.SportType;
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
@RequestMapping("/api/v1/courts")
@AllArgsConstructor
public class CourtController {

    private final CourtService courtService;
    private final AccessControlService accessControl;

    @GetMapping
    public List<CourtResponse> getCourts(
        @RequestParam(required = false) SportType sport,
        Authentication authentication
    ) {
        if (accessControl.isAdmin(authentication)) {
            return courtService.getAllCourts(sport);
        }
        return courtService.getActiveCourts(sport);
    }

    @PostMapping
    public ResponseEntity<CourtResponse> createCourt(
        @Valid @RequestBody CreateCourtRequest request,
        Authentication authentication
    ) {
        accessControl.requireAdmin(authentication);
        return ResponseEntity.ok(courtService.createCourt(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(
        @PathVariable UUID id,
        Authentication authentication
    ) {
        accessControl.requireAdmin(authentication);
        courtService.deleteCourt(id);
        return ResponseEntity.noContent().build();
    }
}

