package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.UsuarioGerente;
import java.sql.*;

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

    public ResultSet Create(UsuarioGerente entity){
        con = getConnection();
        String sql = "INSERT INTO tb_usu (nome, login, senha, is_gerente) VALUES (?, ?, ?, ?)";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());

            ps.execute();
            rs = ps.getGeneratedKeys();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir gerente no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    public ResultSet Read(String param){
        con = getConnection();
        String sql = "SELECT * FROM tb_usu AS e WHERE e.nome = ? AND e.is_gerente = true";
        ResultSet rs = null;

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            rs = ps.executeQuery();
        } catch (SQLException e){
            System.out.println("Erro ao buscar gerente no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet Update(UsuarioGerente entity){
        con = getConnection();
        String sql = "UPDATE tb_usu SET nome = ?, login = ?, senha = ?, is_gerente = ? WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setBoolean(4, entity.isGerente());
            ps.setInt(5, entity.getIdGerente());

            ps.execute();
            rs = ps.getResultSet();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar gerente no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet Delete(UsuarioGerente entity){
        con = getConnection();
        String sql = "DELETE FROM tb_usu WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, entity.getIdGerente());

            ps.execute();
            rs = ps.getResultSet();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar gerente no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }
}