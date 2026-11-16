package com.ghflusao.analytics.dto;

import java.math.BigDecimal;

/** Voo com a quantidade de bilhetes válidos e a receita gerada. */
public record VooProcurado(Integer idVoo, String codigoVoo, String empresa, Long bilhetes, BigDecimal receita) {

    public String receitaFormatada() {
        return Formatos.moeda(receita);
    }
}
