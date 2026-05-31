package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

public class ClienteDAO {

    private final static String URL = "jdbc:mysql://localhost/poo";
    private final static String USER = "root";
    private final static String PASS = "password";
    private static Connection con = null;

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    public Cliente Create(Cliente entity) {
        con = getConnection();
        String sql = "INSERT INTO tb_clientes (cpf, nome, endereco) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, entity.getCpf());
            ps.setString(2, entity.getNome());
            ps.setString(3, entity.getEndereco());

            ps.executeUpdate();
            ps.close();
            return entity;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Cliente ReadByCpf(String cpf) {
        con = getConnection();
        String sql = "SELECT * FROM tb_clientes WHERE cpf = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(rs.getString("cpf"), rs.getString("nome"), rs.getString("endereco"));

                rs.close();
                ps.close();
                return cliente;
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Cliente> ReadByNome(String nome) {
        con = getConnection();
        String sql = "SELECT * FROM tb_clientes WHERE nome LIKE ?";
        List<Cliente> clientes = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nome + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(rs.getString("cpf"), rs.getString("nome"), rs.getString("endereco"));
                clientes.add(cliente);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar clientes no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }

    public List<Cliente> ReadAll() {
        con = getConnection();
        String sql = "SELECT * FROM tb_clientes";
        List<Cliente> clientes = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(rs.getString("cpf"), rs.getString("nome"), rs.getString("endereco"));
                clientes.add(cliente);
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes do banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }

        return clientes;
    }

    public Cliente Update(Cliente entity) {
        con = getConnection();
        String sql = "UPDATE tb_clientes SET nome=?, endereco=? WHERE cpf=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, entity.getNome());
            ps.setString(2, entity.getEndereco());
            ps.setString(3, entity.getCpf());
            int linhasAfetadas = ps.executeUpdate();

            ps.close();

            if (linhasAfetadas > 0) {
                return entity;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean Delete(String cpf) {
        con = getConnection();
        String sql = "DELETE FROM tb_clientes WHERE cpf=?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cpf);
            int linhasAfetadas = ps.executeUpdate();
            ps.close();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente no banco (DAO): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}