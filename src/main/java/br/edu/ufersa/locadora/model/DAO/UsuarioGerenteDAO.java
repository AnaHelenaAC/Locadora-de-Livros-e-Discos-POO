package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.UsuarioGerente;
import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import br.edu.ufersa.locadora.exceptions.UsuarioGerenteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioGerenteDAO {
    public UsuarioGerente Create(UsuarioGerente entity) throws UsuarioGerenteException {
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
                    entity.setIdGerente(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar gerente no banco: " + e.getMessage(), e);
        }

        return entity;
    }

    public List<UsuarioGerente> Read(String param) throws UsuarioGerenteException {
        String sql = "SELECT ID, nome, login, senha, isGerente FROM Usuarios WHERE nome LIKE ? AND isGerente = true";
        List<UsuarioGerente> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + param + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearGerente(rs));
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao buscar gerente no banco: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean Update(UsuarioGerente entity) throws UsuarioGerenteException {
        String sql = "UPDATE Usuarios SET nome = ?, login = ?, senha = ?, isGerente = ? WHERE ID = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.setInt(5, entity.getIdGerente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar gerente no banco: " + e.getMessage(), e);
        }
    }

    public boolean Delete(UsuarioGerente entity) throws UsuarioGerenteException {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdGerente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar gerente no banco: " + e.getMessage(), e);
        }
    }

    private UsuarioGerente mapearGerente(ResultSet rs) throws SQLException {
        try {
            UsuarioGerente gerente = new UsuarioGerente();
            gerente.setIdGerente(rs.getInt("ID"));
            gerente.setNome(rs.getString("nome"));
            gerente.setLogin(rs.getString("login"));
            gerente.setSenha(rs.getString("senha"));
            gerente.setGerente(rs.getBoolean("isGerente"));
            return gerente;
        } catch (SemNomeException | UsuarioException | UsuarioGerenteException e) {
            throw new RuntimeException("Erro ao mapear gerente do banco: " + e.getMessage(), e);
        }
    }
}