package br.edu.ufersa.locadora.model.DAO;

import java.sql.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import br.edu.ufersa.locadora.model.entities.Aluguel;
import br.edu.ufersa.locadora.model.entities.Cliente;

public class AluguelDAO {
    private final static String URL = "jdbc:mysql://localhost/poo";
    private final static String USER = "poo";
    private final static String PASS = "AH443162ah";
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

    public Aluguel Create(Aluguel entity) {
        String sql = "INSERT INTO tb_alugueis (cliente_cpf, data_inicio, data_fim_prevista, data_fim, valor_base, valor_multa) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
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
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                entity.setId(rs.getInt(1));
            }
            rs.close();
            ps.close();

            return entity;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluguel no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Aluguel Update(Aluguel entity) {

        String sql = "UPDATE tb_alugueis SET data_fim = ?, valor_multa = ? WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (entity.getDataFim() != null) {
                ps.setDate(1, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(1, Types.DATE);
            }

            ps.setDouble(2, entity.getValorMulta());
            ps.setInt(3, entity.getId());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                return entity;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluguel no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean Delete(Aluguel entity) {
        String sql = "DELETE FROM tb_alugueis WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, entity.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar aluguel: " + e.getMessage());
            return false;
        }
    }

    public Aluguel Read(int id) {
        String sql = "SELECT * FROM tb_alugueis WHERE id = ?";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClienteDAO clienteDAO = new ClienteDAO();
                    Cliente cliente = clienteDAO.ReadByCpf(rs.getString("cliente_cpf"));

                    // Cria o aluguel usando o construtor
                    return new Aluguel(
                            rs.getInt("id"), cliente, rs.getDate("data_inicio").toLocalDate(),
                            rs.getDate("data_fim_prevista").toLocalDate(),
                            rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                            rs.getDouble("valor_base"), rs.getDouble("valor_multa"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluguel por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Aluguel> ReadAll() {
        List<Aluguel> lista = new ArrayList<>();
        String sql = "SELECT * FROM tb_alugueis";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            ClienteDAO clienteDAO = new ClienteDAO();

            while (rs.next()) {
                Cliente cliente = clienteDAO.ReadByCpf(rs.getString("cliente_cpf"));

                Aluguel aluguel = new Aluguel(
                        rs.getInt("id"),
                        cliente,
                        rs.getDate("data_inicio").toLocalDate(), rs.getDate("data_fim_prevista").toLocalDate(),
                        rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                        rs.getDouble("valor_base"), rs.getDouble("valor_multa"));
                lista.add(aluguel);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar aluguéis: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
