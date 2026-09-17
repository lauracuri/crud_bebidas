package com.template.services;

import com.template.model.dao.BebidasDAO;
import com.template.model.dao.IBebidasDAO;
import com.template.model.dto.BebidasDTO;

import java.util.ArrayList;

/**
 * Camada de serviço de Bebida.
 * Implementa a interface IBebidaService e recebe a dependência do DAO através da interface IBebidasDAO.
 * Aplicação de SOLID:
 * - DIP (Dependency Inversion): Depende da abstração IBebidasDAO, facilitando testes e desacoplamento.
 * - SRP: Encapsula a lógica de negócio do cadastro de bebidas.
 */
public class BebidaService implements IBebidaService {
    private final IBebidasDAO dao;

    // Construtor com injeção de dependência via interface
    public BebidaService(IBebidasDAO dao) {
        this.dao = dao;
    }

    // Construtor padrão para compatibilidade
    public BebidaService() {
        this(new BebidasDAO());
    }

    @Override
    public ArrayList<BebidasDTO> selecionarBebidas() {
        return dao.selecionarBebidas();
    }

    @Override
    public void cadastrarBebida(String marca, String tipo, boolean alcoolica, String sabor) {
        BebidasDTO dto = criarDTO(null, marca, tipo, alcoolica, sabor);
        dao.cadastrarBebida(dto);
    }

    @Override
    public void atualizarBebida(int id, String marca, String tipo, boolean alcoolica, String sabor) {
        BebidasDTO dto = criarDTO(id, marca, tipo, alcoolica, sabor);
        dao.atualizarBebida(dto);
    }

    @Override
    public void excluirBebida(int id) {
        dao.excluirBebida(id);
    }

    private BebidasDTO criarDTO(Integer id, String marca, String tipo, boolean alcoolica, String sabor) {
        BebidasDTO dto = new BebidasDTO();
        if (id != null) dto.setId(id);
        dto.setMarca(marca);
        dto.setTipo(tipo);
        dto.setAlcoolica(alcoolica);
        dto.setSabor(sabor);
        return dto;
    }
}