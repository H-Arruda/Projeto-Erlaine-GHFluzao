package com.ghflusao.service;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.Passageiro;
import com.ghflusao.repository.PassageiroRepository;
import com.ghflusao.repository.BilheteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassageiroService {
    private final PassageiroRepository repository;
    private final BilheteRepository bilheteRepository;

    @Transactional(readOnly = true)
    public List<Passageiro> listar() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public Passageiro buscar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Passageiro não encontrado."));
    }

    @Transactional
    public void salvar(Integer id, String nome, String cpf) {
        nome = nome == null ? "" : nome.trim();
        cpf = cpf == null ? "" : cpf.trim();
        if (nome.isBlank() || nome.length() > 100) throw new BusinessException("Informe um nome com até 100 caracteres.");
        if (!cpf.matches("\\d{11}")) throw new BusinessException("CPF deve conter exatamente 11 números, sem pontuação.");
        if (!cpfValido(cpf)) throw new BusinessException("CPF inválido (dígitos verificadores não conferem).");
        final String cpfFinal = cpf;
        if (repository.findAll().stream().anyMatch(p -> !p.getId().equals(id) && p.getCpf().equals(cpfFinal)))
            throw new BusinessException("Já existe um passageiro com este CPF.");
        Passageiro passageiro = id == null ? new Passageiro() : buscar(id);
        passageiro.setNome(nome);
        passageiro.setCpf(cpf);
        repository.save(passageiro);
    }

    private boolean cpfValido(String cpf) {
        if (cpf.chars().distinct().count() == 1) return false;
        for (int digito = 9; digito < 11; digito++) {
            int soma = 0;
            for (int i = 0; i < digito; i++) soma += Character.digit(cpf.charAt(i), 10) * (digito + 1 - i);
            int resto = (soma * 10) % 11;
            if (resto == 10) resto = 0;
            if (resto != Character.digit(cpf.charAt(digito), 10)) return false;
        }
        return true;
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (bilheteRepository.existsByPassageiroId(id))
            throw new BusinessException("O passageiro possui bilhetes cadastrados.");
        repository.deleteById(id);
    }
}
