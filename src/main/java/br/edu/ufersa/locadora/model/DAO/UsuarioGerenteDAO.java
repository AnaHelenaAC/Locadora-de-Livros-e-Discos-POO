package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.UsuarioGerente;
import br.edu.ufersa.locadora.exceptions.UsuarioGerenteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioGerenteDAO {
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

    public static void closeConnection(){
        if(con != null){
            try{
                con.close();
            }catch (SQLException e){e.printStackTrace();}
        }
    }

    public UsuarioGerente Create(UsuarioGerente entity) throws UsuarioGerenteException {
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
                entity.setIdGerente(rs.getInt(1));
                rs.close();
            }
            ps.close();
        } catch (Exception e) {
            throw new UsuarioGerenteException("Erro ao criar gerente no banco: " + e.getMessage());
        }

        return entity;
    }

    public List<UsuarioGerente> Read(String param) throws UsuarioGerenteException {
        con = getConnection();
        String sql = "SELECT * FROM tb_usu WHERE nome LIKE ? AND is_gerente = true";
        List<UsuarioGerente> lista = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + param + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UsuarioGerente g = new UsuarioGerente();
                g.setIdGerente(rs.getInt("id"));
                try {
                    g.setNome(rs.getString("nome"));
                } catch (Exception e) { e.printStackTrace(); }
                g.setLogin(rs.getString("login"));
                g.setSenha(rs.getString("senha"));

                lista.add(g);
            }
            rs.close();
            ps.close();
        } catch (SQLException e){
            throw new UsuarioGerenteException("Erro ao buscar gerente no banco: " + e.getMessage());
        }
        return lista;
    }

    public boolean Update(UsuarioGerente entity) throws UsuarioGerenteException {
        con = getConnection();
        String sql = "UPDATE tb_usu SET nome = ?, login = ?, senha = ?, is_gerente = ? WHERE id = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.setInt(5, entity.getIdGerente());

            int linhas = ps.executeUpdate();
            if (linhas > 0) sucesso = true;
            ps.close();
        } catch (SQLException e) {
            throw new UsuarioGerenteException("Erro ao atualizar gerente no banco: " + e.getMessage());
        }
        return sucesso;
    }

    public boolean Delete(UsuarioGerente entity) throws UsuarioGerenteException {
        con = getConnection();
        String sql = "DELETE FROM tb_usu WHERE id = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, entity.getIdGerente());

            int líneas = ps.executeUpdate();
            if (líneas > 0) sucesso = true;
            ps.close();
        } catch (SQLException e) {
            throw new UsuarioGerenteException("Erro ao deletar gerente no banco: " + e.getMessage());
        }
        return sucesso;
    }
}