package com.template.model.dto;

public class BebidasDTO {
    private int id;
    private String marca;
    private String tipo;
    private boolean alcoolica;
    private String sabor;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isAlcoolica() { return alcoolica; }
    public void setAlcoolica(boolean alcoolica) { this.alcoolica = alcoolica; }

    public String getSabor() { return sabor; }
    public void setSabor(String sabor) { this.sabor = sabor; }
}