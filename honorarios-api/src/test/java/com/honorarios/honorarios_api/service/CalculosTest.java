package com.honorarios.honorarios_api.service;

import com.honorarios.honorarios_api.entity.Cliente;
import com.honorarios.honorarios_api.entity.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Guarda a logica de status e o calculo de honorarios (base * pct / 100).
class CalculosTest {

    private Cliente cliente(String aReceber, String recebido) {
        Cliente c = new Cliente();
        c.setValorAReceber(new BigDecimal(aReceber));
        if (recebido != null) c.setValorRecebido(new BigDecimal(recebido));
        return c;
    }

    @Test
    void status() {
        assertEquals(Status.PENDENTE, cliente("1000", null).getStatus());
        assertEquals(Status.PENDENTE, cliente("1000", "0").getStatus());
        assertEquals(Status.PARCIAL, cliente("1000", "400").getStatus());
        assertEquals(Status.RECEBIDO, cliente("1000", "1000").getStatus());
        assertEquals(Status.RECEBIDO, cliente("1000", "1200").getStatus());
    }

    @Test
    void honorarios() {
        assertEquals(0, new BigDecimal("300").compareTo(DashboardService.honor(new BigDecimal("3000"), new BigDecimal("10"))));
        assertEquals(0, BigDecimal.ZERO.compareTo(DashboardService.honor(BigDecimal.ZERO, new BigDecimal("10"))));
        assertEquals(0, BigDecimal.ZERO.compareTo(DashboardService.nz(null)));
    }
}
