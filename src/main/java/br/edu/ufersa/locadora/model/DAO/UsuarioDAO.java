package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
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

    public Usuario Create(Usuario entity){
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
                entity.setId(rs.getLong(1));
                rs.close();
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    public List<Usuario> Read(String param){
        con = getConnection();
        String sql = "SELECT * FROM tb_usu WHERE nome LIKE ?";
        List<Usuario> lista = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + param + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setGerente(rs.getBoolean("is_gerente"));
                try {
                    u.setNome(rs.getString("nome"));
                } catch (Exception e) { e.printStackTrace(); }
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));

                lista.add(u);
            }
            rs.close();
            ps.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    public boolean Update(Usuario entity){
        con = getConnection();
        String sql = "UPDATE tb_usu SET nome = ?, login = ?, senha = ?, is_gerente = ? WHERE id = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.setLong(5, entity.getId());

            int linhas = ps.executeUpdate();
            if (linhas > 0) sucesso = true;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sucesso;
    }

    public boolean Delete(Usuario entity){
        con = getConnection();
        String sql = "DELETE FROM tb_usu WHERE id = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, entity.getId());

            int linhas = ps.executeUpdate();
            if (linhas > 0) sucesso = true;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sucesso;
    }
}