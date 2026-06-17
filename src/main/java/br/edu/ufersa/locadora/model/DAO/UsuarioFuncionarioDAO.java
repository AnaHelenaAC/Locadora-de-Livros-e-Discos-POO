package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.UsuarioFuncionario;
import br.edu.ufersa.locadora.exceptions.UsuarioFuncionarioException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFuncionarioDAO {
    private final static String URL = "jdbc:mysql://localhost/poo";
    private final static String USER = "poo";
    private final static String PASS = "AH443162ah";
    private static Connection con = null;

    public static Connection getConnection(){
        if(con == null){
            try{
                con = DriverManager.getConnection(URL,USER,PASS);
            }catch (SQLException e){e.printStackTrace();}
        }
        return con;
    }

    public UsuarioFuncionario Create(UsuarioFuncionario entity) throws UsuarioFuncionarioException {
        con = getConnection();
        String sql = "INSERT INTO tb_usu (nome, login, senha, is_gerente) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                entity.setIdFuncionario(rs.getLong(1));
                rs.close();
            }
            ps.close();
        } catch (Exception e) {
            throw new UsuarioFuncionarioException("Erro ao inserir o funcionário: " + e.getMessage());
        }

        return entity;
    }

    public List<UsuarioFuncionario> Read(String param) throws UsuarioFuncionarioException {
        con = getConnection();
        String sql = "SELECT * FROM tb_usu WHERE nome LIKE ? AND is_gerente = false";
        List<UsuarioFuncionario> lista = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + param + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UsuarioFuncionario f = new UsuarioFuncionario();
                f.setIdFuncionario(rs.getLong("id"));
                f.setGerente(rs.getBoolean("is_gerente"));

                try {
                    f.setNome(rs.getString("nome"));
                    f.setLogin(rs.getString("login"));
                    f.setSenha(rs.getString("senha"));
                } catch (Exception e) { e.printStackTrace(); }


                lista.add(f);
            }

            rs.close();
            ps.close();
        } catch (SQLException e){
            throw new UsuarioFuncionarioException("Erro ao buscar funcionários: " + e.getMessage());
        }
        return lista;
    }

    public boolean Update(UsuarioFuncionario entity) throws UsuarioFuncionarioException {
        con = getConnection();
        String sql = "UPDATE tb_usu SET nome = ?, login = ?, senha = ?, is_gerente = ? WHERE id = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.setLong(5, entity.getIdFuncionario());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                sucesso = true;
            }
            ps.close();
        } catch (SQLException e) {
            throw new UsuarioFuncionarioException("Erro ao atualizar o funcionário: " + e.getMessage());
        }
        return sucesso;
    }

    public boolean Delete(UsuarioFuncionario entity) throws UsuarioFuncionarioException {
        con = getConnection();
        String sql = "DELETE FROM tb_usu WHERE id = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, entity.getIdFuncionario());

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                sucesso = true;
            }
            ps.close();
        } catch (SQLException e) {
            throw new UsuarioFuncionarioException("Erro ao deletar o funcionário: " + e.getMessage());
        }
        return sucesso;
    }
}