package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.ItemAluguel;

import java.sql.*;

public class ItemAluguelDAO {

    private final static String URL = "jdbc:mysql://localhost/poo";
    private final static String USER = "root";
    private final static String PASS = "password";
    private static Connection con = null;

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    public ItemAluguel Create(ItemAluguel entity, int aluguelID) {
        con = getConnection();

        String sql = "INSERT INTO tb_itens_aluguel (aluguel_id, disco_id, livro_id, preco_diaria, dias_alugados) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, aluguelID);

            if (entity.getItem().getIsDisco()) {
                ps.setString(2, entity.getItem().getID());
                ps.setNull(3, Types.BINARY);

            } else {
                ps.setNull(2, Types.BINARY);
                ps.setString(3, entity.getItem().getID());
            }

            ps.setDouble(4, entity.getPrecoDiaria());
            ps.setInt(5, entity.getDiasAlugados());

            ps.executeUpdate();

            ps.close();

            return entity;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir item do aluguel no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}