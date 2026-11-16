package com.ghflusao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voo")
@Getter
@Setter
public class Voo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voo")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "codigo_voo", nullable = false, length = 10)
    private String codigoVoo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SituacaoVoo situacao;

    /** Preço-base: soma de Escala.valor, recalculado no Service. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @OneToMany(mappedBy = "voo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequencial")
    private List<Escala> escalas = new ArrayList<>();

    public void adicionarEscala(Escala escala) {
        escala.setVoo(this);
        escalas.add(escala);
    }
}
