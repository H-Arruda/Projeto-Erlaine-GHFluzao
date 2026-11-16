package com.ghflusao.analytics;

import com.ghflusao.analytics.dto.ResumoGerencial;
import com.ghflusao.analytics.dto.TotalSituacao;
import com.ghflusao.analytics.service.AnalyticsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.ghflusao.model.SituacaoBilhete.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResumoGerencialTest {

    private static ResumoGerencial calcular(TotalSituacao... totais) {
        return ResumoGerencial.de(List.of(totais), AnalyticsService.SITUACOES_RECEITA);
    }

    @Test
    void baseVaziaNaoGeraErroNemDivisaoPorZero() {
        ResumoGerencial r = calcular();
        assertEquals(0L, r.registrados());
        assertEquals(0L, r.validos());
        assertEquals(0L, r.cancelados());
        assertEquals(new BigDecimal("0.00"), r.receitaTotal());
        assertEquals(new BigDecimal("0.00"), r.ticketMedio());
    }

    @Test
    void somenteCanceladosNaoGeramReceita() {
        ResumoGerencial r = calcular(new TotalSituacao(CANCELADO, 2L, new BigDecimal("1100.00")));
        assertEquals(2L, r.registrados());
        assertEquals(0L, r.validos());
        assertEquals(2L, r.cancelados());
        assertEquals(new BigDecimal("0.00"), r.receitaTotal());
        assertEquals(new BigDecimal("0.00"), r.ticketMedio());
    }

    @Test
    void emitidosEUtilizadosGeramReceitaECanceladosNao() {
        ResumoGerencial r = calcular(
                new TotalSituacao(EMITIDO, 2L, new BigDecimal("1100.00")),
                new TotalSituacao(UTILIZADO, 1L, new BigDecimal("280.00")),
                new TotalSituacao(CANCELADO, 1L, new BigDecimal("550.00")));
        assertEquals(4L, r.registrados());
        assertEquals(3L, r.validos());
        assertEquals(1L, r.cancelados());
        assertEquals(new BigDecimal("1380.00"), r.receitaTotal());
        assertEquals(new BigDecimal("460.00"), r.ticketMedio());
    }

    @Test
    void ticketMedioArredondaParaDuasCasas() {
        ResumoGerencial r = calcular(new TotalSituacao(EMITIDO, 3L, new BigDecimal("100.00")));
        assertEquals(new BigDecimal("33.33"), r.ticketMedio());
    }

    @Test
    void valoresNulosSaoTratadosComoZero() {
        ResumoGerencial r = calcular(new TotalSituacao(EMITIDO, null, null));
        assertEquals(0L, r.registrados());
        assertEquals(new BigDecimal("0.00"), r.receitaTotal());
    }

    @Test
    void formatacaoEmReais() {
        ResumoGerencial r = calcular(new TotalSituacao(EMITIDO, 1L, new BigDecimal("1234.5")));
        assertEquals("R$ 1.234,50", r.receitaFormatada());
    }
}
