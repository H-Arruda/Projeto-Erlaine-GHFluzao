package com.ghflusao.analytics.controller;

import com.ghflusao.analytics.dto.ReceitaDiaria;
import com.ghflusao.analytics.dto.ReceitaEmpresa;
import com.ghflusao.analytics.dto.VooProcurado;
import com.ghflusao.analytics.service.AnalyticsService;
import com.ghflusao.exception.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private static final int TOP_VOOS = 5;

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    /** Resumo gerencial: indicadores, gráficos, rankings e consulta por período. */
    @GetMapping
    public String resumo(@RequestParam(name = "dataInicio", required = false) String dataInicio,
                         @RequestParam(name = "dataFim", required = false) String dataFim,
                         Model model) {
        String inicio = dataInicio == null ? "" : dataInicio.trim();
        String fim = dataFim == null ? "" : dataFim.trim();

        if (!inicio.isEmpty() || !fim.isEmpty()) {
            try {
                model.addAttribute("periodo", service.resumoPeriodo(parseData(inicio), parseData(fim)));
            } catch (BusinessException e) {
                model.addAttribute("erroPeriodo", e.getMessage());
            }
        }

        List<VooProcurado> voos = service.voosMaisProcurados(TOP_VOOS);
        List<ReceitaEmpresa> empresas = service.receitaPorEmpresa();
        List<ReceitaDiaria> dias = service.receitaPorDia();

        model.addAttribute("resumo", service.resumo());
        model.addAttribute("voos", voos);
        model.addAttribute("empresas", empresas);
        model.addAttribute("dias", dias);
        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);

        // dados dos gráficos (listas simples, convertidas em JSON pelo Thymeleaf)
        model.addAttribute("diaRotulos", dias.stream().map(ReceitaDiaria::dataFormatada).toList());
        model.addAttribute("diaValores", dias.stream().map(ReceitaDiaria::receita).toList());
        model.addAttribute("vooRotulos", voos.stream().map(VooProcurado::codigoVoo).toList());
        model.addAttribute("vooValores", voos.stream().map(VooProcurado::bilhetes).toList());
        model.addAttribute("empresaRotulos", empresas.stream().map(ReceitaEmpresa::empresa).toList());
        model.addAttribute("empresaValores", empresas.stream().map(ReceitaEmpresa::receita).toList());
        return "analytics/resumo";
    }

    /** Relatório de passageiros de um voo escolhido na lista. */
    @GetMapping("/passageiros-por-voo")
    public String passageirosPorVoo(@RequestParam(name = "vooId", required = false) Integer vooId,
                                    Model model) {
        model.addAttribute("voos", service.listarVoos());
        model.addAttribute("vooId", vooId);
        if (vooId != null) {
            service.passageirosPorVoo(vooId).ifPresentOrElse(
                    relatorio -> model.addAttribute("relatorio", relatorio),
                    () -> model.addAttribute("erro", "Voo não encontrado."));
        }
        return "analytics/passageiros-voo";
    }

    private LocalDate parseData(String texto) {
        if (texto.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException e) {
            throw new BusinessException("Informe datas válidas no formato dia/mês/ano.");
        }
    }
}
