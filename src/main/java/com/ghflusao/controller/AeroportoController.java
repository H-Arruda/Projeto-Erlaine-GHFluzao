package com.ghflusao.controller;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.service.AeroportoService;
import com.ghflusao.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/aeroportos")
@RequiredArgsConstructor
public class AeroportoController {
    private final AeroportoService service;
    private final CidadeRepository cidadeRepository;
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("registros", service.listar());
        return "aeroporto/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        carregarFormulario(model, null, "", "", null);
        return "aeroporto/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            var item = service.buscar(id);
            carregarFormulario(model, id, item.getNome(), item.getSigla(), item.getCidade().getId());
            return "aeroporto/form";
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/aeroportos";
        }
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam String sigla,
                         @RequestParam Integer cidadeId,
                         Model model, RedirectAttributes ra) {
        try {
            service.salvar(id, nome, sigla, cidadeId);
            ra.addFlashAttribute("mensagem", "Aeroporto salvo(a) com sucesso.");
            return "redirect:/aeroportos";
        } catch (BusinessException e) {
            carregarFormulario(model, id, nome, sigla, cidadeId);
            model.addAttribute("erro", e.getMessage());
            return "aeroporto/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("mensagem", "Aeroporto excluído(a).");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/aeroportos";
    }

    private void carregarFormulario(Model model, Integer id, String nome, String sigla, Integer cidadeId) {
        model.addAttribute("id", id);
        model.addAttribute("nome", nome);
        model.addAttribute("sigla", sigla);
        model.addAttribute("cidadeId", cidadeId);
        model.addAttribute("cidades", cidadeRepository.findAll());
    }
}
