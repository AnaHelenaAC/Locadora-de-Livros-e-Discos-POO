package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Registro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Registro Create(Registro entity){
        con = getConnection();
        String sql = "INSERT INTO tb_registro (faturamento_total, id_gerente_logado) VALUES (?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                ps.setInt(2, entity.getGerenteLogado().getIdGerente());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                entity.setIdRegistro(rs.getLong(1));
                rs.close();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public List<Registro> Read(String param){
        con = getConnection();
        String sql = "SELECT * FROM tb_registro WHERE id_registro = ?";
        List<Registro> lista = new ArrayList<>();

        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(param));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Registro reg = new Registro();
                reg.setIdRegistro(rs.getLong("id_registro"));
                reg.setFaturamentoTotal(rs.getDouble("faturamento_total"));
                lista.add(reg);
            }
            rs.close();
            ps.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    public boolean Update(Registro entity){
        con = getConnection();
        String sql = "UPDATE tb_registro SET faturamento_total = ?, id_gerente_logado = ? WHERE id_registro = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                ps.setInt(2, entity.getGerenteLogado().getIdGerente());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setLong(3, entity.getIdRegistro());

            int linhas = ps.executeUpdate();
            if (linhas > 0) sucesso = true;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sucesso;
    }

    public boolean Delete(Registro entity){
        con = getConnection();
        String sql = "DELETE FROM tb_registro WHERE id_registro = ?";
        boolean sucesso = false;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, entity.getIdRegistro());

            int linhas = ps.executeUpdate();
            if (linhas > 0) sucesso = true;
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sucesso;
    }
}