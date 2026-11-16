package com.ghflusao.analytics;

import com.ghflusao.analytics.dto.PontoBilhete;
import com.ghflusao.analytics.dto.ReceitaDiaria;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceitaDiariaTest {

    @Test
    void agrupaPorDiaEOrdenaCronologicamente() {
        List<ReceitaDiaria> dias = ReceitaDiaria.agrupar(List.of(
                new PontoBilhete(LocalDateTime.of(2026, 11, 11, 16, 40), new BigDecimal("660.00")),
                new PontoBilhete(LocalDateTime.of(2026, 11, 10, 9, 30), new BigDecimal("550.00")),
                new PontoBilhete(LocalDateTime.of(2026, 11, 10, 23, 59), new BigDecimal("550.00"))));

        assertEquals(2, dias.size());
        assertEquals(LocalDate.of(2026, 11, 10), dias.get(0).data());
        assertEquals(2L, dias.get(0).bilhetes());
        assertEquals(new BigDecimal("1100.00"), dias.get(0).receita());
        assertEquals(LocalDate.of(2026, 11, 11), dias.get(1).data());
        assertEquals(1L, dias.get(1).bilhetes());
        assertEquals("10/11/2026", dias.get(0).dataFormatada());
    }

    @Test
    void listaVaziaGeraResultadoVazio() {
        assertTrue(ReceitaDiaria.agrupar(List.of()).isEmpty());
    }
}
