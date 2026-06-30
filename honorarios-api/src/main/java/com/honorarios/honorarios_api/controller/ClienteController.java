package com.honorarios.honorarios_api.controller;

import com.honorarios.honorarios_api.dto.ClienteCreateRequest;
import com.honorarios.honorarios_api.dto.ClienteResponse;
import com.honorarios.honorarios_api.dto.ClienteUpdateRequest;
import com.honorarios.honorarios_api.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @GetMapping
    public List<ClienteResponse> listar(@AuthenticationPrincipal UUID usuarioId) {
        return service.listar(usuarioId);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@AuthenticationPrincipal UUID usuarioId, @PathVariable UUID id) {
        return service.buscar(usuarioId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@AuthenticationPrincipal UUID usuarioId,
                                 @Valid @RequestBody ClienteCreateRequest req) {
        return service.criar(usuarioId, req);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@AuthenticationPrincipal UUID usuarioId, @PathVariable UUID id,
                                     @Valid @RequestBody ClienteUpdateRequest req) {
        return service.atualizar(usuarioId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@AuthenticationPrincipal UUID usuarioId, @PathVariable UUID id) {
        service.deletar(usuarioId, id);
    }
}
