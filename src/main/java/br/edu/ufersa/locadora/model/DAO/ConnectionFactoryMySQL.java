package br.edu.ufersa.locadora.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactoryMySQL extends ConnectionFactory {

    private final String url;
    private final String user;
    private final String pass;

    public ConnectionFactoryMySQL(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public Connection createConnection() {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados (MySQL).", e);
        }
    }
}