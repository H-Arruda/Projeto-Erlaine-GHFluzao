package com.ghflusao.analytics.dto;

import com.ghflusao.model.SituacaoBilhete;

import java.math.BigDecimal;

/** Quantidade e soma de valor_total dos bilhetes de uma situação. */
public record TotalSituacao(SituacaoBilhete situacao, Long quantidade, BigDecimal valor) {
}
