package com.ghflusao.controller;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.SituacaoVoo;
import com.ghflusao.repository.AeroportoRepository;
import com.ghflusao.repository.EmpresaRepository;
import com.ghflusao.service.VooService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/voos")
@RequiredArgsConstructor
public class VooController {
    private final VooService service;
    private final EmpresaRepository empresas;
    private final AeroportoRepository aeroportos;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("voos", service.listar());
        return "voo/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("empresas", empresas.findAll());
        return "voo/novo";
    }

    @PostMapping
    public String criar(@RequestParam(required = false) Integer empresaId,
                        @RequestParam(required = false) String codigoVoo,
                        RedirectAttributes ra) {
        try {
            var voo = service.criar(empresaId, codigoVoo, SituacaoVoo.PROGRAMADO);
            ra.addFlashAttribute("mensagem", "Voo criado. Cadastre os trechos.");
            return "redirect:/voos/" + voo.getId();
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/voos/novo";
        }
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("voo", service.buscar(id));
            model.addAttribute("aeroportos", aeroportos.findAll());
            model.addAttribute("situacoes", SituacaoVoo.values());
            return "voo/detalhes";
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/voos";
        }
    }

    @PostMapping("/{id}/escalas")
    public String adicionar(@PathVariable Integer id,
                            @RequestParam(required = false) Integer origemId,
                            @RequestParam(required = false) Integer destinoId,
                            @RequestParam(required = false) Integer tempoEscala,
                            @RequestParam(required = false) BigDecimal valor,
                            RedirectAttributes ra) {
        try {
            service.adicionarEscala(id, origemId, destinoId, tempoEscala, valor);
            ra.addFlashAttribute("mensagem", "Trecho adicionado e preço recalculado.");
        } catch (BusinessException | org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/voos/" + id;
    }

    @PostMapping("/{id}/escalas/remover-ultima")
    public String remover(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.removerUltimaEscala(id);
            ra.addFlashAttribute("mensagem", "Último trecho removido.");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/voos/" + id;
    }

    @PostMapping("/{id}/situacao")
    public String situacao(@PathVariable Integer id, @RequestParam SituacaoVoo situacao, RedirectAttributes ra) {
        try {
            service.alterarSituacao(id, situacao);
            ra.addFlashAttribute("mensagem", "Situação atualizada.");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/voos/" + id;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("mensagem", "Voo excluído.");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/voos";
    }
}
