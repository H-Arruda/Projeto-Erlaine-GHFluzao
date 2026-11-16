package com.ghflusao.analytics.dto;

import com.ghflusao.model.SituacaoBilhete;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

/**
 * Indicadores principais. Equivale ao IndicadoresVendas do módulo Python
 * (faturamento total, quantidade de vendas, ticket médio), com a distinção
 * entre bilhetes válidos (geram receita) e cancelados.
 */
public record ResumoGerencial(long registrados,
                              long validos,
                              long cancelados,
                              BigDecimal receitaTotal,
                              BigDecimal ticketMedio) {

    /**
     * @param totais           agregação por situação (uma linha por situação existente)
     * @param situacoesReceita situações que representam receita
     */
    public static ResumoGerencial de(Collection<TotalSituacao> totais,
                                     Collection<SituacaoBilhete> situacoesReceita) {
        long registrados = 0;
        long validos = 0;
        long cancelados = 0;
        BigDecimal receita = BigDecimal.ZERO;

        for (TotalSituacao t : totais) {
            long qtd = t.quantidade() == null ? 0 : t.quantidade();
            BigDecimal valor = t.valor() == null ? BigDecimal.ZERO : t.valor();
            registrados += qtd;
            if (situacoesReceita.contains(t.situacao())) {
                validos += qtd;
                receita = receita.add(valor);
            } else if (t.situacao() == SituacaoBilhete.CANCELADO) {
                cancelados += qtd;
            }
        }

        BigDecimal receitaTotal = receita.setScale(2, RoundingMode.HALF_UP);
        BigDecimal ticket = validos > 0
                ? receitaTotal.divide(BigDecimal.valueOf(validos), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(2);
        return new ResumoGerencial(registrados, validos, cancelados, receitaTotal, ticket);
    }

    public String receitaFormatada() {
        return Formatos.moeda(receitaTotal);
    }

    public String ticketMedioFormatado() {
        return Formatos.moeda(ticketMedio);
    }
}
