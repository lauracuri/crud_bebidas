package com.template.model.dao;

import com.template.model.dto.BebidasDTO;
import com.template.model.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BebidasDAO {

    public ArrayList<BebidasDTO> selecionarBebidas() {
        ArrayList<BebidasDTO> listaBebidas = new ArrayList<>();
        String sql = "SELECT * FROM bebidas ORDER BY id";

        try (
                Connection c = new Conexao().conectaBD();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                BebidasDTO bebida = new BebidasDTO();
                bebida.setId(rs.getInt("id"));
                bebida.setMarca(rs.getString("marca"));
                bebida.setTipo(rs.getString("tipo"));
                bebida.setAlcoolica(rs.getBoolean("alcoolica"));
                bebida.setSabor(rs.getString("sabor"));

                listaBebidas.add(bebida);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BebidasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaBebidas;
    }

    public void cadastrarBebida(BebidasDTO bebida) {
        String sql = "INSERT INTO bebidas (marca, tipo, alcoolica, sabor) VALUES (?, ?, ?, ?)";

        try (
                Connection c = new Conexao().conectaBD();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, bebida.getMarca());
            ps.setString(2, bebida.getTipo());
            ps.setBoolean(3, bebida.isAlcoolica());
            ps.setString(4, bebida.getSabor());

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BebidasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void atualizarBebida(BebidasDTO bebida) {
        String sql = "UPDATE bebidas SET marca = ?, tipo = ?, alcoolica = ?, sabor = ? WHERE id = ?";

        try (
                Connection c = new Conexao().conectaBD();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, bebida.getMarca());
            ps.setString(2, bebida.getTipo());
            ps.setBoolean(3, bebida.isAlcoolica());
            ps.setString(4, bebida.getSabor());
            ps.setInt(5, bebida.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BebidasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluirBebida(int id) {
        String sql = "DELETE FROM bebidas WHERE id = ?";

        try (
                Connection c = new Conexao().conectaBD();
                PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BebidasDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}