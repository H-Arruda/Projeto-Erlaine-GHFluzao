package com.ghflusao.repository;

import com.ghflusao.model.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AeroportoRepository extends JpaRepository<Aeroporto, Integer> {

    boolean existsByCidadeId(Integer cidadeId);
    @org.springframework.data.jpa.repository.Query("select a from Aeroporto a join fetch a.cidade")
    java.util.List<com.ghflusao.model.Aeroporto> findAllWithCidade();
}
