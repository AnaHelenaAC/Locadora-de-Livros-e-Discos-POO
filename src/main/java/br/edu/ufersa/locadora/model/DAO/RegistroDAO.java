package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Registro;
import java.sql.*;

public class RegistroDAO {
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

    public ResultSet Create(Registro entity){
        con = getConnection();
        String sql = "INSERT INTO tb_registro (faturamento_total, id_gerente_logado) VALUES (?, ?)";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                ps.setInt(2, entity.getGerenteLogado().getIdGerente());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.execute();
            rs = ps.getGeneratedKeys();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir registro no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    public ResultSet Read(String param){
        con = getConnection();
        String sql = "SELECT * FROM tb_registro WHERE id_registro = ?";
        ResultSet rs = null;

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(param));
            rs = ps.executeQuery();
        } catch (SQLException e){
            System.out.println("Erro ao buscar registro no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet Update(Registro entity){
        con = getConnection();
        String sql = "UPDATE tb_registro SET faturamento_total = ?, id_gerente_logado = ? WHERE id_registro = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                ps.setInt(2, entity.getGerenteLogado().getIdGerente());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setLong(3, entity.getIdRegistro());

            ps.execute();
            rs = ps.getResultSet();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar registro no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet Delete(Registro entity){
        con = getConnection();
        String sql = "DELETE FROM tb_registro WHERE id_registro = ?";
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, entity.getIdRegistro());

            ps.execute();
            rs = ps.getResultSet();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar registro no Banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }
}