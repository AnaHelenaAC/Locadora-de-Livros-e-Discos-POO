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
import java.util.ArrayList;
import java.util.List;

public class AluguelDAO {

    public Aluguel Create(Aluguel entity) {
        String sql = "INSERT INTO tb_alugueis (cliente_cpf, data_inicio, data_fim_prevista, valor_base, valor_multa) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getCliente().getCpf());
            ps.setDate(2, Date.valueOf(entity.getDataInicio()));
            ps.setDate(3, Date.valueOf(entity.getDataFimPrevista()));
            ps.setDouble(4, entity.getValorBase());
            ps.setDouble(5, entity.getValorMulta());

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
        String sql = "UPDATE tb_alugueis SET valor_multa = ? WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, entity.getValorMulta());
            ps.setInt(2, entity.getId());

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

                    return montarAluguel(rs, cliente, listaItens);
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
            ItemAluguelDAO itemAluguelDAO = new ItemAluguelDAO();

            while (rs.next()) {
                String cpf = rs.getString("cliente_cpf");
                Cliente cliente = clienteDAO.ReadByCpf(cpf);
                List<ItemAluguel> listaItens = itemAluguelDAO.findByAluguelId(rs.getInt("id"));

                lista.add(montarAluguel(rs, cliente, listaItens));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar aluguéis: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Aluguel> ReadAtivos() {
        List<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT a.* FROM tb_alugueis a JOIN tb_itens_aluguel i ON a.id = i.aluguel_id WHERE i.data_fim IS NULL";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ClienteDAO clienteDAO = new ClienteDAO();
            ItemAluguelDAO itemAluguelDAO = new ItemAluguelDAO();

            while (rs.next()) {
                String cpf = rs.getString("cliente_cpf");
                Cliente cliente = clienteDAO.ReadByCpf(cpf);
                int aluguelId = rs.getInt("id");
                List<ItemAluguel> listaItens = itemAluguelDAO.findByAluguelId(aluguelId);

                lista.add(montarAluguel(rs, cliente, listaItens));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar aluguéis ativos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Monta um Aluguel a partir da linha atual do ResultSet usando o Builder
    private Aluguel montarAluguel(ResultSet rs, Cliente cliente, List<ItemAluguel> listaItens) throws SQLException {
        return Aluguel.builder().id(rs.getInt("id")).cliente(cliente).itensAlugados(listaItens).dataInicio(rs.getDate("data_inicio").toLocalDate()).dataFimPrevista(rs.getDate("data_fim_prevista").toLocalDate()).valorBase(rs.getDouble("valor_base")).valorMulta(rs.getDouble("valor_multa")).build();
    }
}