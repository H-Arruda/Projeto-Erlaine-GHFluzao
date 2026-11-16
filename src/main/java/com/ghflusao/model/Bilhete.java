package com.ghflusao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bilhete")
@Getter
@Setter
public class Bilhete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bilhete")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_voo", nullable = false)
    private Voo voo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_passageiro", nullable = false)
    private Passageiro passageiro;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao;

    /** Preço registrado na emissão; não muda se o voo for reajustado depois. */
    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoBilhete situacao;
}
