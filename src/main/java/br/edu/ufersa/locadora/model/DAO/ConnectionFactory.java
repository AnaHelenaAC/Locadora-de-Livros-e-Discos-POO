package br.edu.ufersa.locadora.model.DAO;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionFactory {
    public abstract Connection createConnection();

    public void testarConexao() {
        try (Connection con = createConnection()) {
            System.out.println("Conexão OK!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
