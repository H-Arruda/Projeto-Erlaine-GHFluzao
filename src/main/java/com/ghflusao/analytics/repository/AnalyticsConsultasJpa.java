package com.ghflusao.analytics.repository;

import com.ghflusao.analytics.dto.*;
import com.ghflusao.model.SituacaoBilhete;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementação com JPQL. Usa o EntityManager (e não @Query em interface) para que
 * um erro de consulta apareça só na página de Analytics e nunca impeça a
 * inicialização do restante do sistema.
 *
 * Todas as consultas usam projeção direta em records (sem carregar entidades),
 * o que evita N+1.
 */
@Repository
public class AnalyticsConsultasJpa implements AnalyticsConsultas {

    private static final String DTO = "com.ghflusao.analytics.dto.";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TotalSituacao> totaisPorSituacao() {
        return em.createQuery(
                "select new " + DTO + "TotalSituacao(b.situacao, count(b), sum(b.valorTotal)) "
                        + "from Bilhete b group by b.situacao", TotalSituacao.class)
                .getResultList();
    }

    @Override
    public List<TotalSituacao> totaisPorSituacao(LocalDateTime inicio, LocalDateTime fimExclusivo) {
        return em.createQuery(
                "select new " + DTO + "TotalSituacao(b.situacao, count(b), sum(b.valorTotal)) "
                        + "from Bilhete b "
                        + "where b.dataEmissao >= :inicio and b.dataEmissao < :fim "
                        + "group by b.situacao", TotalSituacao.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fimExclusivo)
                .getResultList();
    }

    @Override
    public List<VooProcurado> voosMaisProcurados(List<SituacaoBilhete> situacoes, int limite) {
        return em.createQuery(
                "select new " + DTO + "VooProcurado(v.id, v.codigoVoo, emp.nome, count(b), sum(b.valorTotal)) "
                        + "from Bilhete b join b.voo v join v.empresa emp "
                        + "where b.situacao in :situacoes "
                        + "group by v.id, v.codigoVoo, emp.nome "
                        + "order by count(b) desc, v.codigoVoo asc", VooProcurado.class)
                .setParameter("situacoes", situacoes)
                .setMaxResults(limite)
                .getResultList();
    }

    @Override
    public List<ReceitaEmpresa> receitaPorEmpresa(List<SituacaoBilhete> situacoes) {
        return em.createQuery(
                "select new " + DTO + "ReceitaEmpresa(emp.id, emp.nome, count(b), sum(b.valorTotal)) "
                        + "from Bilhete b join b.voo v join v.empresa emp "
                        + "where b.situacao in :situacoes "
                        + "group by emp.id, emp.nome "
                        + "order by sum(b.valorTotal) desc, emp.nome asc", ReceitaEmpresa.class)
                .setParameter("situacoes", situacoes)
                .getResultList();
    }

    @Override
    public List<PontoBilhete> pontosDeReceita(List<SituacaoBilhete> situacoes) {
        return em.createQuery(
                "select new " + DTO + "PontoBilhete(b.dataEmissao, b.valorTotal) "
                        + "from Bilhete b where b.situacao in :situacoes "
                        + "order by b.dataEmissao", PontoBilhete.class)
                .setParameter("situacoes", situacoes)
                .getResultList();
    }

    @Override
    public List<VooOpcao> listarVoos() {
        return em.createQuery(
                "select new " + DTO + "VooOpcao(v.id, v.codigoVoo, emp.nome) "
                        + "from Voo v join v.empresa emp order by v.codigoVoo", VooOpcao.class)
                .getResultList();
    }

    @Override
    public Optional<VooResumo> buscarVoo(Integer idVoo) {
        return em.createQuery(
                "select new " + DTO + "VooResumo(v.id, v.codigoVoo, emp.nome, v.situacao, v.valor) "
                        + "from Voo v join v.empresa emp where v.id = :id", VooResumo.class)
                .setParameter("id", idVoo)
                .getResultList().stream().findFirst();
    }

    @Override
    public List<EscalaRota> escalasDoVoo(Integer idVoo) {
        return em.createQuery(
                "select new " + DTO + "EscalaRota(e.sequencial, o.sigla, d.sigla) "
                        + "from Escala e join e.origem o join e.destino d "
                        + "where e.voo.id = :id order by e.sequencial", EscalaRota.class)
                .setParameter("id", idVoo)
                .getResultList();
    }

    @Override
    public List<PassageiroVoo> passageirosDoVoo(Integer idVoo) {
        return em.createQuery(
                "select new " + DTO + "PassageiroVoo(b.id, p.nome, p.cpf, b.dataEmissao, b.valorTotal, b.situacao) "
                        + "from Bilhete b join b.passageiro p "
                        + "where b.voo.id = :id order by p.nome, b.id", PassageiroVoo.class)
                .setParameter("id", idVoo)
                .getResultList();
    }
}
