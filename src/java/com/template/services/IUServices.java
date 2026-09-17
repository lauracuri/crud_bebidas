package com.template.services;

import javafx.scene.control.TextField;

/**
 * Classe utilitária para configurações da interface de usuário.
 */
public class IUServices {

    public static void configurarCampoNumerico(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
