package br.edu.ufersa.locadora.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private final static String URL = "jdbc:mysql://localhost/locadora_de_discos_e_livros";
    private final static String USER = "poo";
    private final static String PASS = "AH443162ah";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados.", e);
        }
    }
}
