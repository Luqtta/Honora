package com.honorarios.honorarios_api.repository;

import com.honorarios.honorarios_api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    List<Cliente> findByUsuarioId(UUID usuarioId);
    Optional<Cliente> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}
