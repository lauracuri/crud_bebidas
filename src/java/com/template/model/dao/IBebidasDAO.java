package com.template.model.dao;

import com.template.model.dto.BebidasDTO;

import java.util.ArrayList;

/**
 * Interface que abstrai as operações de persistência para Bebidas.
 * Aplicação do Princípio da Inversão de Dependência (DIP) e
 * Princípio da Segregação de Interfaces (ISP).
 */
public interface IBebidasDAO {
    ArrayList<BebidasDTO> selecionarBebidas();
    void cadastrarBebida(BebidasDTO bebida);
    void atualizarBebida(BebidasDTO bebida);
    void excluirBebida(int id);
}
