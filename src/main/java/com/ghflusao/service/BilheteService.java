package com.ghflusao.service;

import com.ghflusao.exception.BusinessException;
import com.ghflusao.model.Bilhete;
import com.ghflusao.model.Passageiro;
import com.ghflusao.model.SituacaoBilhete;
import com.ghflusao.model.SituacaoVoo;
import com.ghflusao.model.Voo;
import com.ghflusao.repository.BilheteRepository;
import com.ghflusao.repository.PassageiroRepository;
import com.ghflusao.repository.VooRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BilheteService {
    private final BilheteRepository bilhetes;
    private final VooRepository voos;
    private final PassageiroRepository passageiros;

    @Transactional(readOnly = true)
    public List<Bilhete> listar() {
        return bilhetes.findAllDetalhados();
    }

    @Transactional(readOnly = true)
    public Bilhete buscar(Integer id) {
        return bilhetes.findDetalhadoById(id)
                .orElseThrow(() -> new BusinessException("Bilhete não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<Voo> voosDisponiveis() {
        return voos.findAllComTrechosParaEmissao();
    }

    @Transactional(readOnly = true)
    public List<Passageiro> passageirosDisponiveis() {
        return passageiros.findAll();
    }

    @Transactional
    public Bilhete emitir(Integer vooId, Integer passageiroId) {
        if (vooId == null) throw new BusinessException("Selecione um voo.");
        if (passageiroId == null) throw new BusinessException("Selecione um passageiro.");
        Voo voo = voos.findDetalhadoById(vooId)
                .orElseThrow(() -> new BusinessException("Voo não encontrado."));
        Passageiro passageiro = passageiros.findById(passageiroId)
                .orElseThrow(() -> new BusinessException("Passageiro não encontrado."));
        if (voo.getSituacao() != SituacaoVoo.PROGRAMADO)
            throw new BusinessException("Só é possível emitir bilhetes para voos programados.");
        if (voo.getEscalas().isEmpty())
            throw new BusinessException("O voo precisa de pelo menos um trecho para emitir bilhetes.");
        if (bilhetes.existsAtivoParaPassageiroEVoo(passageiroId, vooId))
            throw new BusinessException("Este passageiro já possui um bilhete ativo para o voo.");
        BigDecimal preco = voo.getValor();
        if (preco == null || preco.signum() < 0)
            throw new BusinessException("Preço do voo inválido.");
        Bilhete bilhete = new Bilhete();
        bilhete.setVoo(voo);
        bilhete.setPassageiro(passageiro);
        bilhete.setDataEmissao(LocalDateTime.now());
        bilhete.setValorTotal(preco.setScale(2, RoundingMode.UNNECESSARY));
        bilhete.setSituacao(SituacaoBilhete.EMITIDO);
        return bilhetes.save(bilhete);
    }

    @Transactional
    public void alterarSituacao(Integer id, SituacaoBilhete nova) {
        if (nova == null) throw new BusinessException("Selecione uma situação.");
        Bilhete bilhete = bilhetes.findDetalhadoById(id)
                .orElseThrow(() -> new BusinessException("Bilhete não encontrado."));
        SituacaoBilhete atual = bilhete.getSituacao();
        if (atual == nova) return;
        if (atual != SituacaoBilhete.EMITIDO ||
                (nova != SituacaoBilhete.CANCELADO && nova != SituacaoBilhete.UTILIZADO))
            throw new BusinessException("Transição de situação do bilhete não permitida.");
        if (nova == SituacaoBilhete.UTILIZADO && bilhete.getVoo().getSituacao() != SituacaoVoo.CONCLUIDO)
            throw new BusinessException("Um bilhete só pode ser utilizado após a conclusão do voo.");
        bilhete.setSituacao(nova);
    }
}
