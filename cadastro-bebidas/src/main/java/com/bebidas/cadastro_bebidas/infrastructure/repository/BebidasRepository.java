package com.bebidas.cadastro_bebidas.infrastructure.repository;

import com.bebidas.cadastro_bebidas.infrastructure.entitys.Bebidas;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BebidasRepository extends JpaRepository<Bebidas, Integer> {

    Optional<Bebidas> findByMarca(String marca);

    @Transactional
    void delteByMarca(String Marca);
}
