package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Registro;
import br.edu.ufersa.locadora.exceptions.RegistroException;
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
            }catch (SQLException exe){exe.printStackTrace();}
        }
        return con;
    }

    public static void closeConnection(){
        if(con != null){
            try{
                con.close();
            }catch (SQLException exe){exe.printStackTrace();}
        }
    }

    public Registro Create(Registro entity) throws RegistroException {
        con = getConnection();
        String sql = "INSERT INTO tb_registro (faturamento_total, id_gerente_logado) VALUES (?, ?)";

        try {
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                stmt.setInt(2, entity.getGerenteLogado().getIdGerente());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.execute();

            ResultSet rset = stmt.getGeneratedKeys();
            if (rset != null && rset.next()) {
                entity.setIdRegistro(rset.getLong(1));
                rset.close();
            }
            stmt.close();
        } catch (SQLException exe) {
            throw new RegistroException("Erro ao inserir o registro no banco de dados: " + exe.getMessage());
        }

        return entity;
    }

    public List<Registro> Read(String param) throws RegistroException {
        con = getConnection();
        String sql = "SELECT * FROM tb_registro WHERE id_registro = ?";
        List<Registro> lista = new ArrayList<>();

        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, Long.parseLong(param));
            ResultSet rset = stmt.executeQuery();

            while (rset.next()) {
                Registro reg = new Registro();
                reg.setIdRegistro(rset.getLong("id_registro"));
                reg.setFaturamentoTotal(rset.getDouble("faturamento_total"));
                lista.add(reg);
            }
            rset.close();
            stmt.close();
        } catch (Exception exe){
            throw new RegistroException("Erro ao buscar o registro no banco de dados: " + exe.getMessage());
        }
        return lista;
    }

    public boolean Update(Registro entity) throws RegistroException {
        con = getConnection();
        String sql = "UPDATE tb_registro SET faturamento_total = ?, id_gerente_logado = ? WHERE id_registro = ?";
        boolean sucesso = false;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, entity.getFaturamentoTotal());

            if (entity.getGerenteLogado() != null) {
                stmt.setInt(2, entity.getGerenteLogado().getIdGerente());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setLong(3, entity.getIdRegistro());

            int linhas = stmt.executeUpdate();
            if (linhas > 0) sucesso = true;
            stmt.close();
        } catch (SQLException exe) {
            throw new RegistroException("Erro ao atualizar o registro no banco de dados: " + exe.getMessage());
        }
        return sucesso;
    }

    public boolean Delete(Registro entity) throws RegistroException {
        con = getConnection();
        String sql = "DELETE FROM tb_registro WHERE id_registro = ?";
        boolean sucesso = false;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, entity.getIdRegistro());

            int linhas = stmt.executeUpdate();
            if (linhas > 0) sucesso = true;
            stmt.close();
        } catch (SQLException exe) {
            throw new RegistroException("Erro ao deletar o registro no banco de dados: " + exe.getMessage());
        }
        return sucesso;
    }
}