package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final ConnectionFactory connectionFactory;

    public ClienteDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Cliente Create(Cliente entity) {
        String sql = "INSERT INTO tb_clientes (cpf, nome, endereco) VALUES (?, ?, ?)";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getCpf());
            ps.setString(2, entity.getNome());
            ps.setString(3, entity.getEndereco());
            ps.executeUpdate();
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente no banco: " + e.getMessage(), e);
        }
    }

    public Cliente ReadByCpf(String cpf) {
        String sql = "SELECT * FROM tb_clientes WHERE cpf = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(rs.getString("cpf"), rs.getString("nome"), rs.getString("endereco"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente no banco: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Cliente> ReadByNome(String nome) {
        String sql = "SELECT * FROM tb_clientes WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new Cliente(rs.getString("cpf"), rs.getString("nome"), rs.getString("endereco")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes no banco: " + e.getMessage(), e);
        }
        return clientes;
    }

    public List<Cliente> ReadAll() {
        String sql = "SELECT * FROM tb_clientes";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(new Cliente(rs.getString("cpf"), rs.getString("nome"), rs.getString("endereco")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes do banco: " + e.getMessage(), e);
        }

        return clientes;
    }

    public Cliente Update(Cliente entity) {
        String sql = "UPDATE tb_clientes SET nome=?, endereco=? WHERE cpf=?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getCpf());
            if (ps.executeUpdate() > 0) {
                return entity;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente no banco: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean Delete(String cpf) {
        String sql = "DELETE FROM tb_clientes WHERE cpf=?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente no banco: " + e.getMessage(), e);
        }
    }
}