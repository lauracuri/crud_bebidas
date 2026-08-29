package com.template.validator;

import com.template.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class BebidaValidator {

    public boolean validarBebida(String marca, String tipo, String sabor) {
        List<Validador<String>> validadores = new ArrayList<>();

        // Validadores de campos obrigatórios
        validadores.add(new CampoObrigatorioValidador("Marca", marca));
        validadores.add(new CampoObrigatorioValidador("Tipo", tipo));
        validadores.add(new CampoObrigatorioValidador("Sabor", sabor));

        // Validador mais específico (tamanho mínimo do texto)
        validadores.add(new TamanhoMinimoValidador("Marca", marca, 2));
        validadores.add(new TamanhoMinimoValidador("Sabor", sabor, 2));

        for (Validador<String> validador : validadores) {
            if (!validador.validar(validador.getValor())) {
                DialogUtil.showWarning("Aviso de Validação", validador.getMensagemErro());
                return false;
            }
        }
        return true;
    }
}