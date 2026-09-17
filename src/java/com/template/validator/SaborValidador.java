package com.template.validator;

/**
 * Validador específico para o campo de sabor.
 * Garante que a descrição do sabor não contenha apenas dígitos ou caracteres inválidos.
 * Aplica o Princípio da Responsabilidade Única (SRP).
 */
public class SaborValidador implements Validador<String> {
    private final String valor;
    private String mensagemErro;

    public SaborValidador(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean validar(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            this.mensagemErro = "O campo 'Sabor' não pode ser vazio.";
            return false;
        }

        String texto = valor.trim();

        // Verifica se contém ao menos uma letra (evita apenas números como sabor)
        if (!texto.matches(".*[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ].*")) {
            this.mensagemErro = "O sabor deve conter uma descrição textual válida com letras.";
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
