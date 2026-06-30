package com.honorarios.honorarios_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteCreateRequest(
        @NotBlank String nome,
        @NotNull @Positive BigDecimal valorAReceber,
        @PositiveOrZero BigDecimal valorRecebido,
        @DecimalMin("0") @DecimalMax("100") BigDecimal percentualHonorarios,
        LocalDate dataPrevisao
) {}
