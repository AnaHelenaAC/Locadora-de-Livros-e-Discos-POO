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

    public Usuario create(Usuario entity){
        con = getConnection();
        String sql = "INSERT INT tb_usuario (nome, login, senha)" + "VALUES (?, ?, ?)";

        try{
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getLogin());
            ps.setString(3, entity.getSenha());
            ps.execute();
            ps.close();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int idGen = rs.getInt(1);
                entity.setId(Long.valueOf(idGen));
            }

        }catch (SQLException e){e.printStackTrace();}
        return entity;

    }

}
