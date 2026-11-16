package com.ghflusao.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Data de emissão e valor de um bilhete válido (insumo da receita diária). */
public record PontoBilhete(LocalDateTime dataEmissao, BigDecimal valorTotal) {
}
