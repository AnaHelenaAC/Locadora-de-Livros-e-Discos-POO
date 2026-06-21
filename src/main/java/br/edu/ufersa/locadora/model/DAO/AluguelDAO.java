package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.Cliente;
import br.edu.ufersa.locadora.model.entities.ItemAluguel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AluguelDAO {
    public Aluguel Create(Aluguel entity) {
        String sql = "INSERT INTO tb_alugueis (cliente_cpf, data_inicio, data_fim_prevista, data_fim, valor_base, valor_multa) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getCliente().getCpf());
            ps.setDate(2, Date.valueOf(entity.getDataInicio()));
            ps.setDate(3, Date.valueOf(entity.getDataFimPrevista()));

            if (entity.getDataFim() != null) {
                ps.setDate(4, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setDouble(5, entity.getValorBase());
            ps.setDouble(6, entity.getValorMulta());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir aluguel no banco: " + e.getMessage(), e);
        }
    }

    public Aluguel Update(Aluguel entity) {
        String sql = "UPDATE tb_alugueis SET data_fim = ?, valor_multa = ? WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (entity.getDataFim() != null) {
                ps.setDate(1, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(1, Types.DATE);
            }

            ps.setDouble(2, entity.getValorMulta());
            ps.setInt(3, entity.getId());

            if (ps.executeUpdate() > 0) {
                return entity;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluguel no banco: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean Delete(Aluguel entity) {
        String sql = "DELETE FROM tb_alugueis WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar aluguel: " + e.getMessage(), e);
        }
    }

    public Aluguel Read(int id) {
        String sql = "SELECT * FROM tb_alugueis WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ItemAluguelDAO itemAluguelDAO = new ItemAluguelDAO();
                    List<ItemAluguel> listaItens = itemAluguelDAO.findByAluguelId(id);
                    ClienteDAO clienteDAO = new ClienteDAO();
                    String cpf = rs.getString("cliente_cpf");
                    Cliente cliente = clienteDAO.ReadByCpf(cpf);

                    // Cria o aluguel usando o construtor
                    return new Aluguel(
                            rs.getInt("id"),
                            cliente,
                            listaItens,
                            rs.getDate("data_inicio").toLocalDate(),
                            rs.getDate("data_fim_prevista").toLocalDate(),
                            rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                            rs.getDouble("valor_base"), rs.getDouble("valor_multa"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluguel por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Aluguel> ReadAll() {
        List<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_alugueis";

        try (Connection con = ConnectionFactory.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            ClienteDAO clienteDAO = new ClienteDAO();

            while (rs.next()) {
                String cpf = rs.getString("cliente_cpf");
                Cliente cliente = clienteDAO.ReadByCpf(cpf);
                ItemAluguelDAO itemAluguelDAO = new ItemAluguelDAO();
                List<ItemAluguel> listaItens = itemAluguelDAO.findByAluguelId(rs.getInt("id"));

                Aluguel aluguel = new Aluguel(
                        rs.getInt("id"),
                        cliente,
                        listaItens,
                        rs.getDate("data_inicio").toLocalDate(), rs.getDate("data_fim_prevista").toLocalDate(),
                        rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                        rs.getDouble("valor_base"), rs.getDouble("valor_multa"));
                lista.add(aluguel);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar aluguéis: " + e.getMessage(), e);
        }
        return lista;
    }
}
