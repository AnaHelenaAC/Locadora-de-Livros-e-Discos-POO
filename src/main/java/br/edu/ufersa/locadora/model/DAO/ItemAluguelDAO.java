package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.ItemAluguel;
import br.edu.ufersa.locadora.model.entities.Disco;
import br.edu.ufersa.locadora.model.entities.Livro;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class ItemAluguelDAO {

    public ItemAluguel Create(ItemAluguel entity, int aluguelID) {
        String sql = "INSERT INTO tb_itens_aluguel (aluguel_id, disco_id, livro_id, preco_diaria, dias_alugados, data_fim) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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

            // Itens novos começam sem data_fim (nulos)
            if (entity.getDataFim() != null) {
                ps.setDate(6, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.executeUpdate();

            // captura o ID gerado e devolve preenchido na entidade
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }

            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item do aluguel no banco: " + e.getMessage(), e);
        }
    }

    // Atualiza a data_fim usando o id real da linha
    public boolean Update(ItemAluguel entity) {
        if (entity.getId() <= 0) {
            throw new IllegalArgumentException("ItemAluguel sem ID válido para atualização.");
        }

        String sql = "UPDATE tb_itens_aluguel SET data_fim = ? WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (entity.getDataFim() != null) {
                ps.setDate(1, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(1, Types.DATE);
            }
            ps.setInt(2, entity.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar item do aluguel: " + e.getMessage(), e);
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
                    ItemAluguel item;

                    int itemId = rs.getInt("id");
                    String discoId = rs.getString("disco_id");
                    String livroId = rs.getString("livro_id");
                    double precoDiaria = rs.getDouble("preco_diaria");
                    int diasAlugados = rs.getInt("dias_alugados");

                    Date dbDate = rs.getDate("data_fim");
                    LocalDate dataFim = (dbDate != null) ? dbDate.toLocalDate() : null;

                    if (discoId != null) {
                        Disco disco = discoDAO.readByID(discoId);
                        item = ItemAluguel.builder().id(itemId).item(disco).precoDiaria(precoDiaria).diasAlugados(diasAlugados).dataFim(dataFim).build();
                    } else {
                        Livro livro = livroDAO.readByID(livroId);
                        item = ItemAluguel.builder().id(itemId).item(livro).precoDiaria(precoDiaria).diasAlugados(diasAlugados).dataFim(dataFim).build();
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