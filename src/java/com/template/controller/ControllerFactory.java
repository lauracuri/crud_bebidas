package com.template.controller;

import com.template.services.IBebidaService;
import com.template.validator.IBebidaValidador;
import javafx.util.Callback;

/**
 * Fábrica de Controladores (ControllerFactory).
 * Atende aos Requisitos 6 e 7 da professora.
 *
 * Responsável por criar os Controllers e injetar neles as dependências necessárias
 * por meio de suas interfaces (Inversão de Dependência - DIP e Injeção de Dependência).
 * Permite que o MainController não crie diretamente suas próprias dependências.
 */
public class ControllerFactory implements Callback<Class<?>, Object> {

    private final IBebidaService bebidaService;
    private final IBebidaValidador bebidaValidator;

    public ControllerFactory(IBebidaService bebidaService, IBebidaValidador bebidaValidator) {
        this.bebidaService = bebidaService;
        this.bebidaValidator = bebidaValidator;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(bebidaService, bebidaValidator);
        }

        // Caso padrão para outros controllers se houver
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao instanciar o controller: " + controllerClass.getName(), e);
        }
    }
}
