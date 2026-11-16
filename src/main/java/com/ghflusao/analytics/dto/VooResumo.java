package com.ghflusao.analytics.dto;

import com.ghflusao.model.SituacaoVoo;

import java.math.BigDecimal;

/** Cabeçalho do relatório de passageiros por voo. */
public record VooResumo(Integer id, String codigoVoo, String empresa, SituacaoVoo situacao, BigDecimal valor) {

    public String valorFormatado() {
        return Formatos.moeda(valor);
    }
}
