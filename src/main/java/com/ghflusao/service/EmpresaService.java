package com.ghflusao.service;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.Empresa;
import com.ghflusao.repository.EmpresaRepository;
import com.ghflusao.repository.VooRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository repository;
    private final VooRepository vooRepository;

    @Transactional(readOnly = true)
    public List<Empresa> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Empresa buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada."));
    }

    @Transactional
    public Empresa salvar(Empresa empresa) {
        repository.findByCnpj(empresa.getCnpj())
                .filter(outra -> !outra.getId().equals(empresa.getId()))
                .ifPresent(outra -> {
                    throw new BusinessException("Já existe uma empresa com este CNPJ.");
                });
        return repository.save(empresa);
    }

    @Transactional
    public void excluir(Integer id) {
        if (vooRepository.existsByEmpresaId(id)) {
            throw new BusinessException("Não é possível excluir: a empresa possui voos cadastrados.");
        }
        repository.deleteById(id);
    }
}
