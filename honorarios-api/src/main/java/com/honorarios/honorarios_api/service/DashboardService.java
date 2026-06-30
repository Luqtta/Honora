package com.honorarios.honorarios_api.service;

import com.honorarios.honorarios_api.dto.EstimativaResponse;
import com.honorarios.honorarios_api.dto.ResumoResponse;
import com.honorarios.honorarios_api.entity.Cliente;
import com.honorarios.honorarios_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClienteRepository clientes;

    // ponytail: volume baixo (uso pessoal) -> agrega em memoria, sem query agregada no banco.
    public ResumoResponse resumo(UUID usuarioId) {
        BigDecimal totalAReceber = BigDecimal.ZERO;
        BigDecimal totalRecebido = BigDecimal.ZERO;
        BigDecimal honorariosRecebidos = BigDecimal.ZERO;
        BigDecimal honorariosAReceber = BigDecimal.ZERO;

        for (Cliente c : clientes.findByUsuarioId(usuarioId)) {
            BigDecimal aReceber = nz(c.getValorAReceber());
            BigDecimal recebido = nz(c.getValorRecebido());
            BigDecimal pct = nz(c.getPercentualHonorarios());
            BigDecimal pendente = aReceber.subtract(recebido).max(BigDecimal.ZERO);

            totalAReceber = totalAReceber.add(aReceber);
            totalRecebido = totalRecebido.add(recebido);
            honorariosRecebidos = honorariosRecebidos.add(honor(recebido, pct));
            honorariosAReceber = honorariosAReceber.add(honor(pendente, pct));
        }
        return new ResumoResponse(totalAReceber, totalRecebido, honorariosRecebidos, honorariosAReceber);
    }

    public EstimativaResponse estimativa(UUID usuarioId, int ano, Integer mes) {
        BigDecimal total = BigDecimal.ZERO;
        for (Cliente c : clientes.findByUsuarioId(usuarioId)) {
            LocalDate d = c.getDataPrevisao();
            if (d == null || d.getYear() != ano) continue;
            if (mes != null && d.getMonthValue() != mes) continue;
            total = total.add(honor(nz(c.getValorAReceber()), nz(c.getPercentualHonorarios())));
        }
        return new EstimativaResponse(ano, mes, total);
    }

    static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    static BigDecimal honor(BigDecimal base, BigDecimal pct) {
        return base.multiply(pct).movePointLeft(2); // base * pct / 100, sempre exato
    }
}
