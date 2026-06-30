package com.honorarios.honorarios_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

// ponytail: edicao parcial — campo null = "nao mexer". Nao da pra limpar um campo
// nullable via PUT; se um dia precisar, troca por PATCH com Optional/JsonNullable.
public record ClienteUpdateRequest(
        @Size(max = 255) String nome,
        @Positive BigDecimal valorAReceber,
        @PositiveOrZero BigDecimal valorRecebido,
        @DecimalMin("0") @DecimalMax("100") BigDecimal percentualHonorarios,
        LocalDate dataPrevisao
) {}
