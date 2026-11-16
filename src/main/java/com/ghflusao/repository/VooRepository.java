package com.ghflusao.repository;

import com.ghflusao.model.Voo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VooRepository extends JpaRepository<Voo, Integer> {
    boolean existsByCodigoVoo(String codigoVoo);
    boolean existsByEmpresaId(Integer idEmpresa);
}
