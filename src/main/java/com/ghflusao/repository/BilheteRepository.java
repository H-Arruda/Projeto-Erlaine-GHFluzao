package com.ghflusao.repository;

import com.ghflusao.model.Bilhete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BilheteRepository extends JpaRepository<Bilhete, Integer> {
    boolean existsByPassageiroId(Integer passageiroId);
    boolean existsByVooId(Integer vooId);

    @Query("select b from Bilhete b join fetch b.voo v join fetch v.empresa join fetch b.passageiro order by b.id desc")
    List<Bilhete> findAllDetalhados();

    @Query("select b from Bilhete b join fetch b.voo v join fetch v.empresa join fetch b.passageiro where b.id = :id")
    Optional<Bilhete> findDetalhadoById(@Param("id") Integer id);

    @Query("select case when count(b) > 0 then true else false end from Bilhete b " +
           "where b.passageiro.id = :passageiroId and b.voo.id = :vooId " +
           "and b.situacao <> com.ghflusao.model.SituacaoBilhete.CANCELADO")
    boolean existsAtivoParaPassageiroEVoo(@Param("passageiroId") Integer passageiroId,
                                          @Param("vooId") Integer vooId);

     boolean existsByVooIdAndSituacaoIn(
              Integer vooId,
              java.util.Collection<com.ghflusao.model.SituacaoBilhete> situacoes
       );
}
