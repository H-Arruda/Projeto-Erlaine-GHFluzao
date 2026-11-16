package com.ghflusao.analytics.dto;

import java.math.BigDecimal;

/** Receita e bilhetes válidos agrupados por companhia aérea. */
public record ReceitaEmpresa(Integer idEmpresa, String empresa, Long bilhetes, BigDecimal receita) {

    public String receitaFormatada() {
        return Formatos.moeda(receita);
    }
}
