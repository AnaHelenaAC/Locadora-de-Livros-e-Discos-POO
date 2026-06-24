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

    private final ConnectionFactory connectionFactory;
    private final DiscoDAO discoDAO;
    private final LivroDAO livroDAO;

    public ItemAluguelDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.discoDAO = new DiscoDAO(connectionFactory);
        this.livroDAO = new LivroDAO(connectionFactory);
    }

    public ItemAluguel Create(ItemAluguel entity, int aluguelID) {
        String sql = "INSERT INTO tb_itens_aluguel (aluguel_id, disco_id, livro_id, preco_diaria, dias_alugados, data_fim) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = connectionFactory.createConnection();
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

            if (entity.getDataFim() != null) {
                ps.setDate(6, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.executeUpdate();

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

    public boolean Update(ItemAluguel entity) {
        if (entity.getId() <= 0) {
            throw new IllegalArgumentException("ItemAluguel sem ID válido para atualização.");
        }

        String sql = "UPDATE tb_itens_aluguel SET data_fim = ? WHERE id = ?";

        try (Connection con = connectionFactory.createConnection();
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

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, aluguelID);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
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
                        item = new ItemAluguel(itemId, disco, precoDiaria, diasAlugados, dataFim);
                    } else {
                        Livro livro = livroDAO.readByID(livroId);
                        item = new ItemAluguel(itemId, livro, precoDiaria, diasAlugados, dataFim);
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