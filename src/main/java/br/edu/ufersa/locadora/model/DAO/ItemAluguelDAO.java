package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Cliente;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;
import br.edu.ufersa.locadora.model.entities.ItemAcervo;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.model.entities.Livro;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

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
                ps.setNull(3, Types.CHAR);

            } else {
                ps.setNull(2, Types.CHAR);
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

    public List<ItemAluguel> Read(int aluguelID) {
        String sql = "SELECT * FROM tb_itens_aluguel WHERE aluguel_id = ?";
        List<ItemAluguel> itens = new ArrayList<>();

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, aluguelID);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    DiscoDAO discoDAO = new DiscoDAO();
                    LivroDAO livroDAO = new LivroDAO();
                    ItemAluguel item = new ItemAluguel();

                    String discoId = rs.getString("disco_id");
                    String livroId = rs.getString("livro_id");

                    double precoDiaria = rs.getDouble("preco_diaria");
                    int diasAlugados = rs.getInt("dias_alugados");

                    if (discoId != null) {
                        Disco disco = discoDAO.readByID(discoId);
                        item = new ItemAluguel(disco, precoDiaria, diasAlugados);
                    } else {
                        Livro livro = livroDAO.readByID(livroId);
                        item = new ItemAluguel(livro, precoDiaria, diasAlugados);
                    }
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar itens do aluguel: " + e.getMessage());
            e.printStackTrace();
        }
        return itens;
    }

    public List<ItemAluguel> findByAluguelId(int aluguelID) {
        String sql = "SELECT * FROM tb_itens_aluguel WHERE aluguel_id = ?";
        List<ItemAluguel> itens = new ArrayList<>();

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, aluguelID);

            try (ResultSet rs = ps.executeQuery()) {
                DiscoDAO discoDAO = new DiscoDAO();
                LivroDAO livroDAO = new LivroDAO();

                while (rs.next()) {
                    ItemAluguel item = new ItemAluguel();

                    String discoId = rs.getString("disco_id");
                    String livroId = rs.getString("livro_id");

                    double precoDiaria = rs.getDouble("preco_diaria");
                    int diasAlugados = rs.getInt("dias_alugados");

                    if (discoId != null) {
                        Disco disco = discoDAO.readByID(discoId);
                        item = new ItemAluguel(disco, precoDiaria, diasAlugados);
                    } else {
                        Livro livro = livroDAO.readByID(livroId);
                        item = new ItemAluguel(livro, precoDiaria, diasAlugados);
                    }
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar itens do aluguel: " + e.getMessage());
            e.printStackTrace();
        }
        return itens;
    }
}