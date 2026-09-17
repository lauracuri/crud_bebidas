package com.template.validator;

/**
 * Interface que define o contrato de validação para o assunto Bebidas.
 * Atende ao Requisito 4 e ao Princípio da Segregação de Interfaces (ISP),
 * permitindo que os Controllers dependam de uma abstração e não de implementações concretas (DIP).
 */
public interface IBebidaValidador {
    boolean validarBebida(String marca, String tipo, String sabor);
}
