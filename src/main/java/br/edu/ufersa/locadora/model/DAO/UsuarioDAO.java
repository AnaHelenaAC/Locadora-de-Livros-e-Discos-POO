package br.edu.ufersa.locadora.model.DAO;
import br.edu.ufersa.locadora.model.entities.Usuario;
import java.sql.*;
import java.net.URL;

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

    public ResultSet Create(Usuario entity){
        con = getConnection();
        String sql = "INSERT INTO tb_usuario (nome, login, senha) VALUES (?, ?, ?)";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.execute();

            rs = ps.getGeneratedKeys();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    public ResultSet Read(String param){
        con = getConnection();
        String sql = "SELECT * FROM tb_usu AS e WHERE e.nome =?";
        ResultSet rs = null;

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, param);
            ps.execute();
            rs = ps.executeQuery();

        }catch (SQLException e){e.printStackTrace();}
        return rs;
    }

    public ResultSet Update(Usuario entity){
        con = getConnection();
        String sql = "UPDATE tb_usuario SET nome = ?, login = ?, senha = ? WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.setLong(4, entity.getId());
            ps.execute();
            
            rs = ps.getResultSet();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet Delete(Usuario entity){
        con = getConnection();
        String sql = "DELETE FROM tb_usuario WHERE id = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, entity.getId());
            ps.execute();
            rs = ps.getResultSet();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar usuário no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }


}
