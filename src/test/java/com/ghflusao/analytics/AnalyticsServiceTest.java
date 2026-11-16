package com.ghflusao.analytics;

import com.ghflusao.analytics.dto.*;
import com.ghflusao.analytics.repository.AnalyticsConsultas;
import com.ghflusao.analytics.service.AnalyticsService;
import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.SituacaoBilhete;
import com.ghflusao.model.SituacaoVoo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsServiceTest {

    /** Consultas em memória: registra os argumentos recebidos e devolve dados fixos. */
    static class ConsultasFake implements AnalyticsConsultas {
        List<TotalSituacao> totais = new ArrayList<>();
        LocalDateTime inicioRecebido;
        LocalDateTime fimRecebido;
        List<SituacaoBilhete> situacoesRecebidas;
        int limiteRecebido;
        VooResumo voo;

        public List<TotalSituacao> totaisPorSituacao() { return totais; }

        public List<TotalSituacao> totaisPorSituacao(LocalDateTime inicio, LocalDateTime fimExclusivo) {
            inicioRecebido = inicio;
            fimRecebido = fimExclusivo;
            return totais;
        }

        public List<VooProcurado> voosMaisProcurados(List<SituacaoBilhete> situacoes, int limite) {
            situacoesRecebidas = situacoes;
            limiteRecebido = limite;
            return List.of();
        }

        public List<ReceitaEmpresa> receitaPorEmpresa(List<SituacaoBilhete> situacoes) {
            situacoesRecebidas = situacoes;
            return List.of();
        }

        public List<PontoBilhete> pontosDeReceita(List<SituacaoBilhete> situacoes) {
            situacoesRecebidas = situacoes;
            return List.of(new PontoBilhete(LocalDateTime.of(2026, 11, 10, 9, 0), new BigDecimal("550.00")));
        }

        public List<VooOpcao> listarVoos() { return List.of(); }

        public Optional<VooResumo> buscarVoo(Integer idVoo) { return Optional.ofNullable(voo); }

        public List<EscalaRota> escalasDoVoo(Integer idVoo) { return List.of(new EscalaRota(1, "GIG", "CGH")); }

        public List<PassageiroVoo> passageirosDoVoo(Integer idVoo) { return List.of(); }
    }

    private final ConsultasFake consultas = new ConsultasFake();
    private final AnalyticsService service = new AnalyticsService(consultas);

    @Test
    void canceladoNaoEntraNasSituacoesDeReceita() {
        assertTrue(AnalyticsService.SITUACOES_RECEITA.contains(SituacaoBilhete.EMITIDO));
        assertTrue(AnalyticsService.SITUACOES_RECEITA.contains(SituacaoBilhete.UTILIZADO));
        assertFalse(AnalyticsService.SITUACOES_RECEITA.contains(SituacaoBilhete.CANCELADO));
    }

    @Test
    void rankingERecebimentoUsamSomenteSituacoesDeReceita() {
        service.voosMaisProcurados(5);
        assertEquals(AnalyticsService.SITUACOES_RECEITA, consultas.situacoesRecebidas);
        assertEquals(5, consultas.limiteRecebido);
    }

    @Test
    void limiteInvalidoViraUm() {
        service.voosMaisProcurados(0);
        assertEquals(1, consultas.limiteRecebido);
    }

    @Test
    void periodoIncluiODiaFinalInteiro() {
        service.resumoPeriodo(LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 12));
        assertEquals(LocalDateTime.of(2026, 11, 10, 0, 0), consultas.inicioRecebido);
        assertEquals(LocalDateTime.of(2026, 11, 13, 0, 0), consultas.fimRecebido);
    }

    @Test
    void periodoInvertidoOuIncompletoLancaErroDeNegocio() {
        assertThrows(BusinessException.class,
                () -> service.resumoPeriodo(LocalDate.of(2026, 11, 12), LocalDate.of(2026, 11, 10)));
        assertThrows(BusinessException.class, () -> service.resumoPeriodo(null, LocalDate.of(2026, 11, 10)));
        assertThrows(BusinessException.class, () -> service.resumoPeriodo(LocalDate.of(2026, 11, 10), null));
    }

    @Test
    void resumoComBaseVaziaRetornaZeros() {
        ResumoGerencial r = service.resumo();
        assertEquals(new BigDecimal("0.00"), r.receitaTotal());
        assertEquals(new BigDecimal("0.00"), r.ticketMedio());
    }

    @Test
    void receitaPorDiaAgrupaOsPontos() {
        List<ReceitaDiaria> dias = service.receitaPorDia();
        assertEquals(1, dias.size());
        assertEquals(new BigDecimal("550.00"), dias.get(0).receita());
    }

    @Test
    void vooInexistenteRetornaVazio() {
        assertTrue(service.passageirosPorVoo(99).isEmpty());
    }

    @Test
    void vooExistenteMontaRelatorioComRota() {
        consultas.voo = new VooResumo(3, "GH303", "Erlaine GHFlusão", SituacaoVoo.CONCLUIDO, new BigDecimal("280.00"));
        RelatorioPassageirosVoo r = service.passageirosPorVoo(3).orElseThrow();
        assertEquals("GIG → CGH", r.rota());
        assertEquals(0L, r.validos());
    }
}
