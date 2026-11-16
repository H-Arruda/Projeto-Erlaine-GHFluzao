package com.ghflusao.controller;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.Empresa;
import com.ghflusao.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Modelo para os demais CRUDs (Cidade, Aeroporto, Passageiro). */
@Controller
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresas", service.listar());
        return "empresa/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "empresa/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("empresa", service.buscar(id));
            return "empresa/form";
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
            return "redirect:/empresas";
        }
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("empresa") Empresa empresa,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "empresa/form";
        }
        try {
            service.salvar(empresa);
        } catch (BusinessException e) {
            model.addAttribute("erro", e.getMessage());
            return "empresa/form";
        }
        ra.addFlashAttribute("mensagem", "Empresa salva com sucesso.");
        return "redirect:/empresas";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.excluir(id);
            ra.addFlashAttribute("mensagem", "Empresa excluída.");
        } catch (BusinessException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/empresas";
    }
}
