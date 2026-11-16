package com.ghflusao.repository;

import com.ghflusao.model.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VooRepository extends JpaRepository<Voo, Integer> {
    boolean existsByCodigoVoo(String codigoVoo);
    boolean existsByEmpresaId(Integer idEmpresa);

    @Query("select v from Voo v join fetch v.empresa order by v.id desc")
    List<Voo> findAllWithEmpresa();

    @Query("select distinct v from Voo v join fetch v.empresa " +
           "left join fetch v.escalas e left join fetch e.origem left join fetch e.destino " +
           "where v.id = :id")
    Optional<Voo> findDetalhadoById(@Param("id") Integer id);

    @Query("select distinct v from Voo v join fetch v.empresa " +
           "where v.situacao = com.ghflusao.model.SituacaoVoo.PROGRAMADO " +
           "and exists (select 1 from Escala e where e.voo = v) order by v.codigoVoo")
    List<Voo> findAllComTrechosParaEmissao();
}
