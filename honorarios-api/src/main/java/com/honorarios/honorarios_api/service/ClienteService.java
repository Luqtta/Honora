package com.honorarios.honorarios_api.service;

import com.honorarios.honorarios_api.dto.ClienteCreateRequest;
import com.honorarios.honorarios_api.dto.ClienteResponse;
import com.honorarios.honorarios_api.dto.ClienteUpdateRequest;
import com.honorarios.honorarios_api.entity.Cliente;
import com.honorarios.honorarios_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clientes;

    public List<ClienteResponse> listar(UUID usuarioId) {
        return clientes.findByUsuarioId(usuarioId).stream().map(ClienteResponse::from).toList();
    }

    public ClienteResponse buscar(UUID usuarioId, UUID id) {
        return ClienteResponse.from(getOwned(usuarioId, id));
    }

    public ClienteResponse criar(UUID usuarioId, ClienteCreateRequest req) {
        Cliente c = new Cliente();
        c.setUsuarioId(usuarioId);
        c.setNome(req.nome());
        c.setValorAReceber(req.valorAReceber());
        c.setValorRecebido(req.valorRecebido());
        c.setPercentualHonorarios(req.percentualHonorarios());
        c.setDataPrevisao(req.dataPrevisao());
        return ClienteResponse.from(clientes.save(c));
    }

    public ClienteResponse atualizar(UUID usuarioId, UUID id, ClienteUpdateRequest req) {
        Cliente c = getOwned(usuarioId, id);
        if (req.nome() != null) c.setNome(req.nome());
        if (req.valorAReceber() != null) c.setValorAReceber(req.valorAReceber());
        if (req.valorRecebido() != null) c.setValorRecebido(req.valorRecebido());
        if (req.percentualHonorarios() != null) c.setPercentualHonorarios(req.percentualHonorarios());
        if (req.dataPrevisao() != null) c.setDataPrevisao(req.dataPrevisao());
        return ClienteResponse.from(clientes.save(c));
    }

    public void deletar(UUID usuarioId, UUID id) {
        clientes.delete(getOwned(usuarioId, id));
    }

    private Cliente getOwned(UUID usuarioId, UUID id) {
        return clientes.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado"));
    }
}
