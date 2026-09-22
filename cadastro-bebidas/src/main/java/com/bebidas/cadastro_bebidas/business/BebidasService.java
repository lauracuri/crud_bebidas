package com.bebidas.cadastro_bebidas.business;

import com.bebidas.cadastro_bebidas.infrastructure.entitys.Bebidas;
import com.bebidas.cadastro_bebidas.infrastructure.repository.BebidasRepository;
import org.springframework.stereotype.Service;

@Service
public class BebidasService {
    private final BebidasRepository repository;

    public BebidasService(BebidasRepository repository) {
        this.repository = repository;
    }

    public void salvarBebidas(Bebidas bebidas){
        repository.saveAndFlush(bebidas);
    }

    public Bebidas buscarBebidasPorMarca(String marca){
        return repository.findByMarca(marca).orElseThrow(
                () -> new RuntimeException("Marca não encontrada")
        );
    }
}
