package com.alvar.oasisclub.schedule.controller;

import com.alvar.oasisclub.auth.security.AccessControlService;
import com.alvar.oasisclub.schedule.service.ScheduleSlotService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedule")
@AllArgsConstructor
public class ScheduleController {

  private final ScheduleSlotService scheduleSlotService;
  private final AccessControlService accessControl;

  @GetMapping
  public List<String> getSlots() {
    return scheduleSlotService.getAllSlots();
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> addSlot(
      @RequestBody Map<String, String> body,
      Authentication authentication
  ) {
    accessControl.requireAdmin(authentication);
    String time = body.get("time");
    if (time == null || time.isBlank()) {
      throw new IllegalArgumentException("El campo 'time' es obligatorio");
    }
    String created = scheduleSlotService.addSlot(time);
    return ResponseEntity.ok(Map.of("time", created));
  }

  @DeleteMapping("/{time}")
  public ResponseEntity<Void> removeSlot(
      @PathVariable String time,
      Authentication authentication
  ) {
    accessControl.requireAdmin(authentication);
    scheduleSlotService.removeSlot(time);
    return ResponseEntity.noContent().build();
  }
}
