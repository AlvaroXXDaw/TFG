package com.alvar.oasisclub.clients.repository;

import com.alvar.oasisclub.clients.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {
  Optional<ClientEntity> findByEmailIgnoreCase(String email);

  Optional<ClientEntity> findByPhone(String phone);

  Page<ClientEntity> findAllByOrderByJoinDateDesc(Pageable pageable);

  Page<ClientEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByJoinDateDesc(
      String name,
      String email,
      Pageable pageable
  );
}


