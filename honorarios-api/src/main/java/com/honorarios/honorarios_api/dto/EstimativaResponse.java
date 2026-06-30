package com.honorarios.honorarios_api.dto;

import java.math.BigDecimal;

public record EstimativaResponse(
        int ano,
        Integer mes,
        BigDecimal honorariosEstimados
) {}
