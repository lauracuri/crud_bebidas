package com.template.validator;

import com.template.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por orquestrar a validação dos campos de Bebida.
 * Implementa a interface IBebidaValidador (Requisito 4).
 *
 * Aplicação de SOLID:
 * - SRP: Responsável exclusivamente por coordenar o conjunto de validações de Bebida.
 * - OCP: Novos validadores podem ser adicionados à lista sem modificar o Controller.
 * - LSP: Qualquer classe que implementa Validador<String> é aceita e processada uniformemente.
 * - DIP: O Controller depende da interface IBebidaValidador e não desta classe concreta.
 */
public class BebidaValidador implements IBebidaValidador {

    @Override
    public boolean validarBebida(String marca, String tipo, String sabor) {
        // 1. Possui uma lista genérica contendo os validadores (Requisito 3)
        List<Validador<String>> validadores = new ArrayList<>();

        // 2. Armazena as validações dos diferentes campos (Requisito 3)
        // Validadores de campos obrigatórios (Requisito 2)
        validadores.add(new CamposObrigatoriosValidador("Marca", marca));
        validadores.add(new CamposObrigatoriosValidador("Tipo", tipo));
        validadores.add(new CamposObrigatoriosValidador("Sabor", sabor));

        // Validadores adicionais com regras de negócio específicas (Requisito 2)
        validadores.add(new TamanhoMinimoValidador("Marca", marca, 2));
        validadores.add(new TamanhoMinimoValidador("Sabor", sabor, 2));
        validadores.add(new TipoBebidaValidador(tipo));
        validadores.add(new SaborValidador(sabor));

        // 3. Executa as validações percorrendo a lista com a estrutura foreach obrigatória (Requisito 3)
        for (Validador<String> validador : validadores) {
            // 4. Utiliza os métodos definidos pela interface Validador: validar, getValor e getMensagemErro
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning("Aviso de Validação", validador.getMensagemErro());
                return false; // Retorna falso na primeira violação de regra
            }
        }

        return true; // Todos os validadores foram atendidos com sucesso
    }
}
