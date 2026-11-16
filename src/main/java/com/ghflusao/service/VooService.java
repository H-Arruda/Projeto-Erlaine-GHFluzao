package com.ghflusao.service;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.*;
import com.ghflusao.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VooService {
    private final VooRepository voos;
    private final EmpresaRepository empresas;
    private final AeroportoRepository aeroportos;
    private final EscalaRepository escalas;
    private final BilheteRepository bilhetes;

    @Transactional(readOnly = true)
    public List<Voo> listar() { return voos.findAllWithEmpresa(); }

    @Transactional(readOnly = true)
    public Voo buscar(Integer id) {
        return voos.findDetalhadoById(id)
                .orElseThrow(() -> new BusinessException("Voo não encontrado."));
    }

    @Transactional
    public Voo criar(Integer empresaId, String codigo, SituacaoVoo situacao) {
        if (empresaId == null) throw new BusinessException("Selecione a empresa.");
        if (codigo == null || codigo.isBlank()) throw new BusinessException("Informe o código do voo.");
        codigo = codigo.trim().toUpperCase(java.util.Locale.ROOT);
        if (codigo.length() > 10) throw new BusinessException("Código do voo deve ter no máximo 10 caracteres.");
        if (voos.existsByCodigoVoo(codigo)) throw new BusinessException("Código de voo já cadastrado.");
        if (situacao != SituacaoVoo.PROGRAMADO) throw new BusinessException("Um novo voo deve começar como PROGRAMADO.");
        Empresa empresa = empresas.findById(empresaId)
                .orElseThrow(() -> new BusinessException("Empresa não encontrada."));
        Voo voo = new Voo();
        voo.setEmpresa(empresa);
        voo.setCodigoVoo(codigo);
        voo.setSituacao(situacao);
        voo.setValor(BigDecimal.ZERO.setScale(2));
        return voos.save(voo);
    }

    @Transactional
    public void adicionarEscala(Integer vooId, Integer origemId, Integer destinoId,
                               Integer tempoMinutos, BigDecimal valor) {
        Voo voo = buscar(vooId);
        if (voo.getSituacao() != SituacaoVoo.PROGRAMADO)
            throw new BusinessException("Só é possível alterar escalas de voos programados.");
        if (bilhetes.existsByVooId(vooId))
            throw new BusinessException("O itinerário não pode ser alterado após a emissão de bilhetes.");
        if (origemId == null || destinoId == null) throw new BusinessException("Selecione origem e destino.");
        if (origemId.equals(destinoId)) throw new BusinessException("Origem e destino devem ser diferentes.");
        if (tempoMinutos == null || tempoMinutos <= 0) throw new BusinessException("Duração deve ser maior que zero.");
        if (valor == null || valor.signum() < 0 || valor.scale() > 2 || valor.compareTo(new BigDecimal("99999999.99")) > 0)
            throw new BusinessException("Informe um valor entre 0 e 99999999,99, com até duas casas decimais.");
        Aeroporto origem = aeroportos.findById(origemId)
                .orElseThrow(() -> new BusinessException("Aeroporto de origem não encontrado."));
        Aeroporto destino = aeroportos.findById(destinoId)
                .orElseThrow(() -> new BusinessException("Aeroporto de destino não encontrado."));
        List<Escala> existentes = escalas.findByVooIdOrderBySequencialAsc(vooId);
        if (!existentes.isEmpty() && !existentes.get(existentes.size() - 1).getDestino().getId().equals(origemId))
            throw new BusinessException("O início do novo trecho deve coincidir com o destino do anterior.");
        Escala trecho = new Escala();
        trecho.setVoo(voo);
        trecho.setOrigem(origem);
        trecho.setDestino(destino);
        trecho.setSequencial(existentes.size() + 1);
        trecho.setTempoEscala(tempoMinutos);
        trecho.setValor(valor.setScale(2, RoundingMode.UNNECESSARY));
        BigDecimal total = existentes.stream().map(Escala::getValor).reduce(BigDecimal.ZERO, BigDecimal::add).add(trecho.getValor());
        if (total.compareTo(new BigDecimal("99999999.99")) > 0)
            throw new BusinessException("O preço total excede o limite do banco de dados.");
        escalas.save(trecho);
        voo.setValor(total.setScale(2));
        voos.save(voo);
    }

    @Transactional
    public void removerUltimaEscala(Integer vooId) {
        Voo voo = buscar(vooId);
        if (voo.getSituacao() != SituacaoVoo.PROGRAMADO || bilhetes.existsByVooId(vooId))
            throw new BusinessException("Não é possível modificar o itinerário deste voo.");
        List<Escala> existentes = escalas.findByVooIdOrderBySequencialAsc(vooId);
        if (existentes.isEmpty()) throw new BusinessException("Este voo não tem escalas.");
        Escala ultima = existentes.get(existentes.size() - 1);
        voo.getEscalas().removeIf(e -> e.getId().equals(ultima.getId()));
        voo.setValor(voo.getValor().subtract(ultima.getValor()).setScale(2));
        voos.save(voo);
    }

    @Transactional
    public void alterarSituacao(Integer vooId, SituacaoVoo nova) {
        Voo voo = buscar(vooId);
        if (nova == null) throw new BusinessException("Selecione uma situação.");
        if (voo.getSituacao() == nova) return;
        if (voo.getEscalas().isEmpty()) throw new BusinessException("Cadastre um trecho antes de alterar a situação.");
        boolean permitida = switch (voo.getSituacao()) {
            case PROGRAMADO -> nova == SituacaoVoo.EM_ANDAMENTO || nova == SituacaoVoo.CANCELADO;
            case EM_ANDAMENTO -> nova == SituacaoVoo.CONCLUIDO;
            default -> false;
        };
        if (!permitida) throw new BusinessException("Transição de situação não permitida.");
        if (nova == SituacaoVoo.CANCELADO &&
                bilhetes.existsByVooIdAndSituacaoIn(
                    vooId,
                    List.of(SituacaoBilhete.EMITIDO, SituacaoBilhete.UTILIZADO)
                )) {
            throw new BusinessException(
                "Existem bilhetes ativos ou utilizados. " +
                "Regularize-os antes de cancelar o voo."
            );
}
        voo.setSituacao(nova);
        voos.save(voo);
    }

    @Transactional
    public void excluir(Integer id) {
        Voo voo = buscar(id);
        if (bilhetes.existsByVooId(id)) throw new BusinessException("Não é possível excluir voo com bilhetes.");
        if (voo.getSituacao() != SituacaoVoo.PROGRAMADO) throw new BusinessException("Só é possível excluir voos programados.");
        voos.delete(voo);
    }
}
