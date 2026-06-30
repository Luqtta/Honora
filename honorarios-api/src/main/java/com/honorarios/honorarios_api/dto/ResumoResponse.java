package com.honorarios.honorarios_api.dto;

import java.math.BigDecimal;

public record ResumoResponse(
        BigDecimal totalAReceber,
        BigDecimal totalRecebido,
        BigDecimal honorariosRecebidos,
        BigDecimal honorariosAReceber
) {}
