package com.honorarios.honorarios_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteCreateRequest(
        @NotBlank @Size(max = 255) String nome,
        @NotNull @Positive @Digits(integer = 13, fraction = 2) BigDecimal valorAReceber,
        @PositiveOrZero @Digits(integer = 13, fraction = 2) BigDecimal valorRecebido,
        @DecimalMin("0") @DecimalMax("100") BigDecimal percentualHonorarios,
        LocalDate dataPrevisao
) {}
