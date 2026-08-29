package com.template.validator;

public class CampoObrigatorioValidador implements Validador<String> {
    private final String nomeCampo;
    private final String valor;
    private String mensagemErro;

    public CampoObrigatorioValidador(String nomeCampo, String valor) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
    }

    @Override
    public boolean validar(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            this.mensagemErro = "O campo '" + nomeCampo + "' é obrigatório.";
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