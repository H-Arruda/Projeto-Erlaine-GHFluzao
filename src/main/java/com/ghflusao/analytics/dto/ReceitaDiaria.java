package com.ghflusao.analytics.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/** Bilhetes válidos e receita de um dia (equivale a vendas_por_dia do módulo Python). */
public record ReceitaDiaria(LocalDate data, long bilhetes, BigDecimal receita) {

    public static List<ReceitaDiaria> agrupar(List<PontoBilhete> pontos) {
        TreeMap<LocalDate, long[]> contagem = new TreeMap<>();
        TreeMap<LocalDate, BigDecimal> soma = new TreeMap<>();
        for (PontoBilhete p : pontos) {
            LocalDate dia = p.dataEmissao().toLocalDate();
            contagem.computeIfAbsent(dia, d -> new long[1])[0]++;
            soma.merge(dia, p.valorTotal() == null ? BigDecimal.ZERO : p.valorTotal(), BigDecimal::add);
        }
        List<ReceitaDiaria> resultado = new ArrayList<>();
        contagem.forEach((dia, qtd) ->
                resultado.add(new ReceitaDiaria(dia, qtd[0], soma.get(dia).setScale(2, RoundingMode.HALF_UP))));
        return resultado;
    }

    public String dataFormatada() {
        return Formatos.data(data);
    }

    public String receitaFormatada() {
        return Formatos.moeda(receita);
    }
}
