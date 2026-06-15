package com.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    static String conexao = "jdbc:postgresql://localhost:5432/bebidas";
    static String usuario = "postgres";
    static String senha = "postgres";

    public Connection conectaBD() {
        try {
            return DriverManager.getConnection(conexao, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}