package com.template.validator;

public class TamanhoMinimoValidador implements Validador<String> {
    private final String nomeCampo;
    private final String valor;
    private final int tamanhoMinimo;
    private String mensagemErro;

    public TamanhoMinimoValidador(String nomeCampo, String valor, int tamanhoMinimo) {
        this.nomeCampo = nomeCampo;
        this.valor = valor;
        this.tamanhoMinimo = tamanhoMinimo;
    }

    @Override
    public boolean validar(String valor) {
        if (valor == null || valor.trim().length() < tamanhoMinimo) {
            this.mensagemErro = "O campo '" + nomeCampo + "' deve ter pelo menos " + tamanhoMinimo + " caracteres.";
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