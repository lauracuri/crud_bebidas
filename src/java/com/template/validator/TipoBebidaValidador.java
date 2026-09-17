package com.template.validator;

import java.util.Arrays;
import java.util.List;

/**
 * Validador específico para o tipo de bebida.
 * Garante que a categoria selecionada seja válida dentro do catálogo do sistema.
 * Demonstra o Princípio da Responsabilidade Única (SRP) e facilidade de extensão (OCP).
 */
public class TipoBebidaValidador implements Validador<String> {
    private final String valor;
    private String mensagemErro;

    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList(
            "Refrigerante",
            "Suco",
            "Água",
            "Energético",
            "Cerveja",
            "Vinho",
            "Destilado",
            "Outro"
    );

    public TipoBebidaValidador(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean validar(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            this.mensagemErro = "Selecione um tipo de bebida válido.";
            return false;
        }

        boolean tipoExiste = TIPOS_PERMITIDOS.stream()
                .anyMatch(tipo -> tipo.equalsIgnoreCase(valor.trim()));

        if (!tipoExiste) {
            this.mensagemErro = "O tipo de bebida '" + valor + "' não é uma categoria válida.";
            return false;
        }

        return true;
    }

    @Override
    public String getMensagemErro() {
        return this.mensagemErro;
    }

    @Override
    public String getValor() {
        return this.valor;
    }
}
