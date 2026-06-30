package com.honorarios.honorarios_api.controller;

import com.honorarios.honorarios_api.dto.EstimativaResponse;
import com.honorarios.honorarios_api.dto.ResumoResponse;
import com.honorarios.honorarios_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping("/resumo")
    public ResumoResponse resumo(@AuthenticationPrincipal UUID usuarioId) {
        return service.resumo(usuarioId);
    }

    @GetMapping("/estimativa")
    public EstimativaResponse estimativa(@AuthenticationPrincipal UUID usuarioId,
                                         @RequestParam int ano,
                                         @RequestParam(required = false) Integer mes) {
        return service.estimativa(usuarioId, ano, mes);
    }
}
