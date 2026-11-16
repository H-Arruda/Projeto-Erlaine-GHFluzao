package com.ghflusao.service;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.Aeroporto;
import com.ghflusao.repository.AeroportoRepository;
import com.ghflusao.repository.CidadeRepository;
import com.ghflusao.repository.EscalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AeroportoService {
    private final AeroportoRepository repository;
    private final CidadeRepository cidadeRepository;
    private final EscalaRepository escalaRepository;

    @Transactional(readOnly = true)
    public List<Aeroporto> listar() { return repository.findAllWithCidade(); }

    @Transactional(readOnly = true)
    public Aeroporto buscar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Aeroporto não encontrado."));
    }

    @Transactional
    public void salvar(Integer id, String nome, String sigla, Integer cidadeId) {
        nome = nome == null ? "" : nome.trim();
        sigla = sigla == null ? "" : sigla.trim().toUpperCase();
        if (nome.isBlank() || nome.length() > 100) throw new BusinessException("Informe um nome com até 100 caracteres.");
        if (!sigla.matches("[A-Z]{3}")) throw new BusinessException("A sigla deve ter três letras, como GIG.");
        if (cidadeId == null) throw new BusinessException("Selecione uma cidade.");
        final String siglaFinal = sigla;
        if (repository.findAll().stream().anyMatch(a -> !a.getId().equals(id) && a.getSigla().equalsIgnoreCase(siglaFinal)))
            throw new BusinessException("Essa sigla já está cadastrada.");
        var cidade = cidadeRepository.findById(cidadeId).orElseThrow(() -> new BusinessException("Cidade não encontrada."));
        Aeroporto aeroporto = id == null ? new Aeroporto() : buscar(id);
        aeroporto.setNome(nome);
        aeroporto.setSigla(sigla);
        aeroporto.setCidade(cidade);
        repository.save(aeroporto);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (escalaRepository.existsByOrigemIdOrDestinoId(id, id))
            throw new BusinessException("O aeroporto é utilizado em trechos de voo.");
        repository.deleteById(id);
    }
}
