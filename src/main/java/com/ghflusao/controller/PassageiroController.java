package com.ghflusao.controller;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.service.PassageiroService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/passageiros")
@RequiredArgsConstructor
public class PassageiroController {
    private final PassageiroService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("registros", service.listar());
        return "passageiro/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        carregarFormulario(model, null, "", "");
        return "passageiro/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            var item = service.buscar(id);
            carregarFormulario(model, id, item.getNome(), item.getCpf());
            return "passageiro/form";
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/passageiros";
        }
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam String cpf,
                         Model model, RedirectAttributes ra) {
        try {
            service.salvar(id, nome, cpf);
            ra.addFlashAttribute("mensagem", "Passageiro salvo(a) com sucesso.");
            return "redirect:/passageiros";
        } catch (BusinessException e) {
            carregarFormulario(model, id, nome, cpf);
            model.addAttribute("erro", e.getMessage());
            return "passageiro/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("mensagem", "Passageiro excluído(a).");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/passageiros";
    }

    private void carregarFormulario(Model model, Integer id, String nome, String cpf) {
        model.addAttribute("id", id);
        model.addAttribute("nome", nome);
        model.addAttribute("cpf", cpf);

    }
}
