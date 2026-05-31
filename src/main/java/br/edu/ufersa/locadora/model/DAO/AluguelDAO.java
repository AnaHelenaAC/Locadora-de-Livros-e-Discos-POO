package br.edu.ufersa.locadora.model.DAO;
import java.sql.*;
import java.net.URL;

import br.edu.ufersa.locadora.model.entities.Aluguel;

import java.sql.*;

public class AluguelDAO {
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

    public Aluguel Create(Aluguel entity) {
        con = getConnection();
        String sql = "INSERT INTO tb_alugueis (cliente_cpf, data_inicio, data_fim_prevista, data_fim, valor_base, valor_multa) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, entity.getCliente().getCpf());
            ps.setDate(2, Date.valueOf(entity.getDataInicio()));
            ps.setDate(3, Date.valueOf(entity.getDataFimPrevista()));

            if (entity.getDataFim() != null) {
                ps.setDate(4, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setDouble(5, entity.getValorBase());
            ps.setDouble(6, entity.getValorMulta());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                entity.setID(rs.getInt(1));
            }

            rs.close();
            ps.close();

            return entity;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluguel no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public Aluguel Update(Aluguel entity) {
        con = getConnection();

        String sql = "UPDATE tb_alugueis SET data_fim = ?, valor_multa = ? WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            if (entity.getDataFim() != null) {
                ps.setDate(1, Date.valueOf(entity.getDataFim()));
            } else {
                ps.setNull(1, Types.DATE);
            }

            ps.setDouble(2, entity.getValorMulta());
            ps.setInt(3, entity.getID());

            int linhasAfetadas = ps.executeUpdate();

            ps.close();

            if (linhasAfetadas > 0) {
                return entity;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluguel no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
