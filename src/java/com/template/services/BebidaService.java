package com.template.services;

import com.template.model.dao.BebidasDAO;
import com.template.model.dto.BebidasDTO;

import java.util.ArrayList;

public class BebidaService {
    private final BebidasDAO dao = new BebidasDAO();

    public ArrayList<BebidasDTO> selecionarBebidas() {
        return dao.selecionarBebidas();
    }

    public void cadastrarBebida(String marca, String tipo, boolean alcoolica, String sabor) {
        BebidasDTO dto = criarDTO(null, marca, tipo, alcoolica, sabor);
        dao.cadastrarBebida(dto);
    }

    public void atualizarBebida(int id, String marca, String tipo, boolean alcoolica, String sabor) {
        BebidasDTO dto = criarDTO(id, marca, tipo, alcoolica, sabor);
        dao.atualizarBebida(dto);
    }

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