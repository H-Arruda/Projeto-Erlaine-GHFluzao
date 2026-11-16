package com.ghflusao.controller;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.service.CidadeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {
    private final CidadeService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("registros", service.listar());
        return "cidade/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        carregarFormulario(model, null, "", "");
        return "cidade/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            var item = service.buscar(id);
            carregarFormulario(model, id, item.getNome(), item.getUf());
            return "cidade/form";
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cidades";
        }
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam String uf,
                         Model model, RedirectAttributes ra) {
        try {
            service.salvar(id, nome, uf);
            ra.addFlashAttribute("mensagem", "Cidade salvo(a) com sucesso.");
            return "redirect:/cidades";
        } catch (BusinessException e) {
            carregarFormulario(model, id, nome, uf);
            model.addAttribute("erro", e.getMessage());
            return "cidade/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("mensagem", "Cidade excluído(a).");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/cidades";
    }

    private void carregarFormulario(Model model, Integer id, String nome, String uf) {
        model.addAttribute("id", id);
        model.addAttribute("nome", nome);
        model.addAttribute("uf", uf);

    }
}
