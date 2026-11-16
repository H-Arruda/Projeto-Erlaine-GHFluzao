package com.ghflusao.repository;

import com.ghflusao.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EscalaRepository extends JpaRepository<Escala, Integer> {
    boolean existsByOrigemIdOrDestinoId(Integer origemId, Integer destinoId);
    List<Escala> findByVooIdOrderBySequencialAsc(Integer vooId);
}
