package com.alvar.oasisclub.clients.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "clients")
public class ClientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(nullable = false, length = 180, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private ClientPlan plan;

  @Column(name = "join_date", nullable = false)
  private LocalDate joinDate;

  @Column(name = "subscription_name", nullable = false, length = 80)
  private String subscriptionName;

  @Column(name = "next_billing_date", nullable = false)
  private LocalDate nextBillingDate;

  @Column(name = "subscription_amount_cents", nullable = false)
  private Integer subscriptionAmountCents;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false, length = 20)
  private String role;

  @Column(length = 20, unique = true)
  private String phone;

  @Column(name = "birth_date")
  private LocalDate birthDate;
}


