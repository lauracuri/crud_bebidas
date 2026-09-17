package com.template;

import com.template.controller.ControllerFactory;
import com.template.model.dao.BebidasDAO;
import com.template.model.dao.IBebidasDAO;
import com.template.services.BebidaService;
import com.template.services.IBebidaService;
import com.template.validator.BebidaValidador;
import com.template.validator.IBebidaValidador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Ponto de entrada da aplicação JavaFX.
 * Realiza a configuração de dependências e fábrica de controladores (Requisito 7).
 *
 * Aplicação de SOLID:
 * - DIP (Inversão de Dependência): Instancia as classes concretas na inicialização do sistema
 *   e as injeta nos componentes dependentes através de suas respectivas interfaces.
 * - Injeção de Dependência: A instância de BebidaValidador é criada externamente e injetada no
 *   MainController através da ControllerFactory.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));

        // 1. Instanciação das dependências concretas fora do Controller
        IBebidasDAO dao = new BebidasDAO();
        IBebidaService bebidaService = new BebidaService(dao);
        IBebidaValidador bebidaValidator = new BebidaValidador();

        // 2. Configuração da Fábrica de Controladores com as dependências (Requisitos 6 e 7)
        ControllerFactory factory = new ControllerFactory(bebidaService, bebidaValidator);
        loader.setControllerFactory(factory);

        Parent root = loader.load();
        Scene scene = new Scene(root, 980, 660);

        stage.setTitle("🍹 Sistema de Gerenciamento de Bebidas");
        stage.setMinWidth(995);
        stage.setMinHeight(690);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}