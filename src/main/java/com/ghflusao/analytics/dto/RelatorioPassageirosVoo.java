package com.ghflusao.analytics.dto;

import com.ghflusao.model.SituacaoBilhete;

import java.util.List;

/** Relatório de passageiros de um voo, com a rota montada a partir das escalas. */
public record RelatorioPassageirosVoo(VooResumo voo,
                                      String rota,
                                      List<PassageiroVoo> passageiros,
                                      long validos,
                                      long cancelados) {

    public static RelatorioPassageirosVoo de(VooResumo voo, List<EscalaRota> escalas, List<PassageiroVoo> passageiros) {
        long cancelados = passageiros.stream().filter(p -> p.situacao() == SituacaoBilhete.CANCELADO).count();
        return new RelatorioPassageirosVoo(voo, montarRota(escalas), passageiros,
                passageiros.size() - cancelados, cancelados);
    }

    /** "GIG → CNF → SSA": origem da primeira escala e destino de cada escala, na ordem do sequencial. */
    public static String montarRota(List<EscalaRota> escalas) {
        if (escalas == null || escalas.isEmpty()) {
            return "";
        }
        StringBuilder rota = new StringBuilder(escalas.get(0).origem());
        for (EscalaRota e : escalas) {
            rota.append(" → ").append(e.destino());
        }
        return rota.toString();
    }
}
