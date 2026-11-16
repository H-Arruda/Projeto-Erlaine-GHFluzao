package com.ghflusao.service;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.Cidade;
import com.ghflusao.repository.CidadeRepository;
import com.ghflusao.repository.AeroportoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CidadeService {
    private final CidadeRepository repository;
    private final AeroportoRepository aeroportoRepository;

    @Transactional(readOnly = true)
    public List<Cidade> listar() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public Cidade buscar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Cidade não encontrada."));
    }

    @Transactional
    public void salvar(Integer id, String nome, String uf) {
        nome = nome == null ? "" : nome.trim();
        uf = uf == null ? "" : uf.trim().toUpperCase();
        if (nome.isBlank() || nome.length() > 100) throw new BusinessException("Informe um nome com até 100 caracteres.");
        if (!uf.matches("[A-Z]{2}")) throw new BusinessException("A UF deve conter duas letras.");
        final String nomeFinal = nome, ufFinal = uf;
        boolean duplicada = repository.findAll().stream().anyMatch(c ->
            !c.getId().equals(id) && c.getNome().equalsIgnoreCase(nomeFinal)
            && c.getUf().equalsIgnoreCase(ufFinal));
        if (duplicada) throw new BusinessException("Cidade e UF já cadastradas.");
        Cidade cidade = id == null ? new Cidade() : buscar(id);
        cidade.setNome(nome);
        cidade.setUf(uf);
        repository.save(cidade);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        if (aeroportoRepository.existsByCidadeId(id))
            throw new BusinessException("A cidade possui aeroportos cadastrados.");
        repository.deleteById(id);
    }
}
