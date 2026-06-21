package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.ItemAluguel;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.model.entities.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ItemAluguelDAO {

    public ItemAluguel Create(ItemAluguel entity, int aluguelID) {
        String sql = "INSERT INTO tb_itens_aluguel (aluguel_id, disco_id, livro_id, preco_diaria, dias_alugados) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
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

            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item do aluguel no banco: " + e.getMessage(), e);
        }
    }

    public List<ItemAluguel> Read(int aluguelID) {
        String sql = "SELECT * FROM tb_itens_aluguel WHERE aluguel_id = ?";
        List<ItemAluguel> itens = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
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
            throw new RuntimeException("Erro ao buscar itens do aluguel: " + e.getMessage(), e);
        }
        return itens;
    }

    public List<ItemAluguel> findByAluguelId(int aluguelID) {
        return Read(aluguelID);
    }
}