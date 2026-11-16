package com.ghflusao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@Getter
@Setter
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer id;

    @NotBlank(message = "Informe o nome")
    @Size(max = 100, message = "Máximo de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Informe o CNPJ")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter 14 dígitos, sem pontuação")
    @Column(nullable = false, length = 14)
    private String cnpj;
}
