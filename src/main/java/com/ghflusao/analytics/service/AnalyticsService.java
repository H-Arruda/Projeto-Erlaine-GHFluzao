package com.ghflusao.analytics.service;

import com.ghflusao.analytics.dto.*;
import com.ghflusao.analytics.repository.AnalyticsConsultas;
import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.SituacaoBilhete;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Regras e cálculos dos indicadores. Somente leitura.
 *
 * Regra de receita (adaptada do módulo Python, que só contava vendas FINALIZADAS):
 * bilhetes EMITIDO e UTILIZADO geram receita; CANCELADO não.
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    /** Única definição de "bilhete que gera receita": ajuste aqui se a regra mudar. */
    public static final List<SituacaoBilhete> SITUACOES_RECEITA =
            List.of(SituacaoBilhete.EMITIDO, SituacaoBilhete.UTILIZADO);

    private final AnalyticsConsultas consultas;

    public AnalyticsService(AnalyticsConsultas consultas) {
        this.consultas = consultas;
    }

    /** Receita total, bilhetes registrados/válidos/cancelados e ticket médio (todo o histórico). */
    public ResumoGerencial resumo() {
        return ResumoGerencial.de(consultas.totaisPorSituacao(), SITUACOES_RECEITA);
    }

    /** Mesmos indicadores, restritos aos bilhetes emitidos no período (dia final incluído). */
    public ResumoGerencial resumoPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new BusinessException("Informe a data inicial e a data final.");
        }
        if (inicio.isAfter(fim)) {
            throw new BusinessException("A data inicial não pode ser posterior à data final.");
        }
        return ResumoGerencial.de(
                consultas.totaisPorSituacao(inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay()),
                SITUACOES_RECEITA);
    }

    /** Ranking por quantidade de bilhetes válidos; empate resolvido pelo código do voo. */
    public List<VooProcurado> voosMaisProcurados(int limite) {
        return consultas.voosMaisProcurados(SITUACOES_RECEITA, Math.max(1, limite));
    }

    public List<ReceitaEmpresa> receitaPorEmpresa() {
        return consultas.receitaPorEmpresa(SITUACOES_RECEITA);
    }

    public List<ReceitaDiaria> receitaPorDia() {
        return ReceitaDiaria.agrupar(consultas.pontosDeReceita(SITUACOES_RECEITA));
    }

    public List<VooOpcao> listarVoos() {
        return consultas.listarVoos();
    }

    /** Passageiros do voo (todas as situações, cancelados identificados) e rota; vazio se o voo não existe. */
    public Optional<RelatorioPassageirosVoo> passageirosPorVoo(Integer idVoo) {
        return consultas.buscarVoo(idVoo).map(voo -> RelatorioPassageirosVoo.de(
                voo, consultas.escalasDoVoo(idVoo), consultas.passageirosDoVoo(idVoo)));
    }
}
