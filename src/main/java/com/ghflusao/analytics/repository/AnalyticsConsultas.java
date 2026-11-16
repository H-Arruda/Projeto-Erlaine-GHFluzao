package com.ghflusao.analytics.repository;

import com.ghflusao.analytics.dto.*;
import com.ghflusao.model.SituacaoBilhete;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** Consultas de leitura do módulo Analytics (não altera nenhum dado). */
public interface AnalyticsConsultas {

    /** Quantidade e soma por situação, de todo o histórico. */
    List<TotalSituacao> totaisPorSituacao();

    /** Quantidade e soma por situação, com data_emissao em [inicio, fimExclusivo). */
    List<TotalSituacao> totaisPorSituacao(LocalDateTime inicio, LocalDateTime fimExclusivo);

    List<VooProcurado> voosMaisProcurados(List<SituacaoBilhete> situacoes, int limite);

    List<ReceitaEmpresa> receitaPorEmpresa(List<SituacaoBilhete> situacoes);

    List<PontoBilhete> pontosDeReceita(List<SituacaoBilhete> situacoes);

    List<VooOpcao> listarVoos();

    Optional<VooResumo> buscarVoo(Integer idVoo);

    List<EscalaRota> escalasDoVoo(Integer idVoo);

    List<PassageiroVoo> passageirosDoVoo(Integer idVoo);
}
