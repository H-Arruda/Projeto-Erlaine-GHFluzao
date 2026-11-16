package com.ghflusao.controller;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.SituacaoBilhete;
import com.ghflusao.service.BilheteService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bilhetes")
@RequiredArgsConstructor
public class BilheteController {
    private final BilheteService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("bilhetes", service.listar());
        return "bilhete/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("voos", service.voosDisponiveis());
        model.addAttribute("passageiros", service.passageirosDisponiveis());
        return "bilhete/novo";
    }

    @PostMapping
    public String emitir(@RequestParam(required = false) Integer vooId,
                         @RequestParam(required = false) Integer passageiroId,
                         RedirectAttributes ra) {
        try {
            var bilhete = service.emitir(vooId, passageiroId);
            ra.addFlashAttribute("mensagem", "Bilhete emitido com sucesso.");
            return "redirect:/bilhetes/" + bilhete.getId();
        } catch (BusinessException | DataIntegrityViolationException e) {
            ra.addFlashAttribute("erro", e instanceof BusinessException ? e.getMessage() : "Não foi possível emitir o bilhete.");
            return "redirect:/bilhetes/novo";
        }
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("bilhete", service.buscar(id));
            return "bilhete/detalhes";
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/bilhetes";
        }
    }

    @PostMapping("/{id}/situacao")
    public String alterarSituacao(@PathVariable Integer id,
                                 @RequestParam(required = false) SituacaoBilhete situacao,
                                 RedirectAttributes ra) {
        try {
            service.alterarSituacao(id, situacao);
            ra.addFlashAttribute("mensagem", "Situação do bilhete atualizada.");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/bilhetes/" + id;
    }
}
