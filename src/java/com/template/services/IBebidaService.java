package com.template.services;

import com.template.model.dto.BebidasDTO;

import java.util.ArrayList;

/**
 * Interface que define o contrato de serviço para manipulação de Bebidas.
 * Aplica o Princípio da Segregação de Interfaces (ISP) e Inversão de Dependência (DIP).
 */
public interface IBebidaService {
    ArrayList<BebidasDTO> selecionarBebidas();
    void cadastrarBebida(String marca, String tipo, boolean alcoolica, String sabor);
    void atualizarBebida(int id, String marca, String tipo, boolean alcoolica, String sabor);
    void excluirBebida(int id);
}
