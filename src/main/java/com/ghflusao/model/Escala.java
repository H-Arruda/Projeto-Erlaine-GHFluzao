package com.ghflusao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/** Trecho de um voo entre dois aeroportos. */
@Entity
@Table(name = "escala")
@Getter
@Setter
public class Escala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escala")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voo", nullable = false)
    private Voo voo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_aeroporto_origem", nullable = false)
    private Aeroporto origem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_aeroporto_destino", nullable = false)
    private Aeroporto destino;

    @Column(nullable = false)
    private int sequencial;

    /** Duração do trecho, em minutos. */
    @Column(name = "tempo_escala", nullable = false)
    private int tempoEscala;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}
