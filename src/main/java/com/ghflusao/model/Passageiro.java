package com.ghflusao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "passageiro")
@Getter
@Setter
public class Passageiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passageiro")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;
}
