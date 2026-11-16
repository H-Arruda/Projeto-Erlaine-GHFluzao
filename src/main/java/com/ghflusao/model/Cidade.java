package com.ghflusao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cidade")
@Getter
@Setter
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cidade")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 2)
    private String uf;
}
