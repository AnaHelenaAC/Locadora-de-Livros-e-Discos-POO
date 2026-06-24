package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.exceptions.SemNomeException;
import br.edu.ufersa.locadora.exceptions.UsuarioException;
import br.edu.ufersa.locadora.model.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final ConnectionFactory connectionFactory;

    public UsuarioDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Usuario Create(Usuario entity) throws UsuarioException {
        String sql = "INSERT INTO Usuarios (nome, login, senha) VALUES (?, ?, ?)";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar usuário no banco: " + e.getMessage(), e);
        }

        return entity;
    }

    public List<Usuario> Read(String param) {
        String sql = "SELECT ID, nome, login, senha FROM Usuarios WHERE nome LIKE ? OR login LIKE ?";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + param + "%";
            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ler dados de usuário: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean Update(Usuario entity) {
        String sql = "UPDATE Usuarios SET nome = ?, login = ?, senha = ? WHERE ID = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setLong(4, entity.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dados de usuário: " + e.getMessage(), e);
        }
    }

    public boolean Delete(Usuario entity) {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuário do banco: " + e.getMessage(), e);
        }
    }

    public Usuario ReadGerente() {
        String sql = "SELECT ID, nome, login, senha FROM Usuarios WHERE ID = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, Usuario.ID_GERENTE);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gerente no banco: " + e.getMessage(), e);
        }
        return null;
    }

    public Usuario ReadPorId(Long id) {
        String sql = "SELECT ID, nome, login, senha FROM Usuarios WHERE ID = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Usuario> ReadFuncionarios(String param) {
        String sql = "SELECT ID, nome, login, senha FROM Usuarios " +
                "WHERE ID != ? AND (nome LIKE ? OR login LIKE ?)";
        List<Usuario> lista = new ArrayList<>();

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, Usuario.ID_GERENTE); // Passa o ID 1L para ignorar o gerente
            String like = "%" + param + "%";
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários no banco: " + e.getMessage(), e);
        }
        return lista;
    }

    public Usuario ReadPorLogin(String login) {
        String sql = "SELECT ID, nome, login, senha FROM Usuarios WHERE login = ?";

        try (Connection con = connectionFactory.createConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, login);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por login: " + e.getMessage(), e);
        }
        return null;
    }

    private Usuario mapearUsuario(ResultSet rs) {
        try {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("ID"));
            usuario.setNome(rs.getString("nome"));
            usuario.setLogin(rs.getString("login"));
            usuario.setSenha(rs.getString("senha"));
            return usuario;
        } catch (SemNomeException | UsuarioException e) {
            throw new RuntimeException("Erro ao mapear usuário do banco: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ler usuário do banco: " + e.getMessage(), e);
        }
    }
}