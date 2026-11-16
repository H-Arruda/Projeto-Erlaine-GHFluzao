package com.ghflusao.analytics;

import com.ghflusao.analytics.dto.*;
import com.ghflusao.model.SituacaoBilhete;
import com.ghflusao.model.SituacaoVoo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RelatorioPassageirosVooTest {

    @Test
    void montaARotaPelaSequenciaDasEscalas() {
        String rota = RelatorioPassageirosVoo.montarRota(List.of(
                new EscalaRota(1, "GIG", "CNF"),
                new EscalaRota(2, "CNF", "SSA")));
        assertEquals("GIG → CNF → SSA", rota);
    }

    @Test
    void semEscalasARotaFicaVazia() {
        assertEquals("", RelatorioPassageirosVoo.montarRota(List.of()));
    }

    @Test
    void contaValidosECancelados() {
        VooResumo voo = new VooResumo(1, "GH101", "Erlaine GHFlusão", SituacaoVoo.PROGRAMADO, new BigDecimal("550.00"));
        LocalDateTime agora = LocalDateTime.of(2026, 11, 10, 9, 30);
        RelatorioPassageirosVoo r = RelatorioPassageirosVoo.de(voo, List.of(new EscalaRota(1, "GIG", "CNF")), List.of(
                new PassageiroVoo(1, "Ana Souza", "11144477735", agora, new BigDecimal("550.00"), SituacaoBilhete.EMITIDO),
                new PassageiroVoo(2, "Bruno Lima", "52998224725", agora, new BigDecimal("550.00"), SituacaoBilhete.CANCELADO)));
        assertEquals(1L, r.validos());
        assertEquals(1L, r.cancelados());
        assertEquals("GIG → CNF", r.rota());
        assertEquals("111.444.777-35", r.passageiros().get(0).cpfFormatado());
    }
}
