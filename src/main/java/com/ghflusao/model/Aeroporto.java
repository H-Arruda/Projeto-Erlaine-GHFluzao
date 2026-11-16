package com.ghflusao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aeroporto")
@Getter
@Setter
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeroporto")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 3)
    private String sigla;
}
