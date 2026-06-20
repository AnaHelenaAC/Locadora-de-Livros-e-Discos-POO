package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import br.edu.ufersa.locadora.exceptions.UsuarioFuncionarioException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFuncionarioDAO {
    public UsuarioFuncionario Create(UsuarioFuncionario entity) throws UsuarioFuncionarioException {
        String sql = "INSERT INTO Usuarios (nome, login, senha, isGerente) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setIdFuncionario(rs.getLong(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir o funcionário: " + e.getMessage(), e);
        }

        return entity;
    }

    public List<UsuarioFuncionario> Read(String param) throws UsuarioFuncionarioException {
        String sql = "SELECT ID, nome, login, senha, isGerente FROM Usuarios WHERE nome LIKE ? AND isGerente = false";
        List<UsuarioFuncionario> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + param + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFuncionario(rs));
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao buscar funcionários: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean Update(UsuarioFuncionario entity) throws UsuarioFuncionarioException {
        String sql = "UPDATE Usuarios SET nome = ?, login = ?, senha = ?, isGerente = ? WHERE ID = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.setLong(5, entity.getIdFuncionario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o funcionário: " + e.getMessage(), e);
        }
    }

    public boolean Delete(UsuarioFuncionario entity) throws UsuarioFuncionarioException {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, entity.getIdFuncionario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o funcionário: " + e.getMessage(), e);
        }
    }

    private UsuarioFuncionario mapearFuncionario(ResultSet rs) throws SQLException {
        try {
            UsuarioFuncionario funcionario = new UsuarioFuncionario();
            funcionario.setIdFuncionario(rs.getLong("ID"));
            funcionario.setGerente(rs.getBoolean("isGerente"));
            funcionario.setNome(rs.getString("nome"));
            funcionario.setLogin(rs.getString("login"));
            funcionario.setSenha(rs.getString("senha"));
            return funcionario;
        } catch (SemNomeException | UsuarioException | UsuarioFuncionarioException e) {
            throw new RuntimeException("Erro ao mapear funcionário do banco: " + e.getMessage(), e);
        }
    }
}