package com.honorarios.honorarios_api.dto;

import com.honorarios.honorarios_api.entity.Cliente;
import com.honorarios.honorarios_api.entity.Status;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ClienteResponse(
        UUID id,
        String nome,
        BigDecimal valorAReceber,
        BigDecimal valorRecebido,
        BigDecimal percentualHonorarios,
        LocalDate dataPrevisao,
        Status status,
        Instant createdAt,
        Instant updatedAt
) {
    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(
                c.getId(), c.getNome(), c.getValorAReceber(), c.getValorRecebido(),
                c.getPercentualHonorarios(), c.getDataPrevisao(), c.getStatus(),
                c.getCreatedAt(), c.getUpdatedAt());
    }
}
