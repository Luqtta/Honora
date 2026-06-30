package com.honorarios.honorarios_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal valorAReceber;

    private BigDecimal valorRecebido;

    private BigDecimal percentualHonorarios;

    private LocalDate dataPrevisao;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Transient
    public Status getStatus() {
        if (valorRecebido == null || valorRecebido.signum() == 0) return Status.PENDENTE;
        if (valorRecebido.compareTo(valorAReceber) >= 0) return Status.RECEBIDO;
        return Status.PARCIAL;
    }
}
