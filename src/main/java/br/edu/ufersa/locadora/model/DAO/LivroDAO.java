package br.edu.ufersa.locadora.model.DAO;

import br.edu.ufersa.locadora.model.entities.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    // Declaração das credenciais que estavam faltando
    private final static String URL = "jdbc:mysql://localhost/poo";
    private final static String USER = "poo";
    private final static String PASS = "AH443162ah";

    // Método auxiliar opcional se você não quiser usar o ConnectionFactory externo
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void create(Livro livro) {
        String query = "INSERT INTO Livros (titulo, criadoPor, genero, valor, dataDeLancamento, qtdItens, qtdPaginas) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getCriadoPor());
            ps.setString(3, livro.getGenero());
            ps.setDouble(4, livro.getValor());
            ps.setObject(5, livro.getDataDeLancamento());
            ps.setInt(6, livro.getQtdItens());
            ps.setInt(7, livro.getQtdPaginas());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Alterado para listar TODOS os livros (sem filtro de ID)
    public List<Livro> read() {
        String query = "SELECT * FROM Livros";
        List<Livro> livros = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) { // Aqui funciona porque não tem "?" na Query

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("criadoPor"),
                        rs.getString("genero"),
                        rs.getDouble("valor"),
                        rs.getString("dataDeLancamento"), // Cuidado: verifique se o construtor de Livro aceita String ou Date!
                        rs.getInt("qtdItens"),
                        rs.getBoolean("isLivro"),
                        rs.getInt("qtdPaginas")
                );
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    // Corrigido o retorno para Livro (estava Disco)
    public Livro readByID(String ID) {
        String query = "SELECT * FROM Livros WHERE ID = ?";
        Livro livro = null;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, ID); // Setamos o ID primeiro...

            try (ResultSet rs = ps.executeQuery()) { // ...e executamos a query depois!
                if (rs.next()) {
                    livro = new Livro(
                            rs.getString("titulo"),
                            rs.getString("criadoPor"),
                            rs.getString("genero"),
                            rs.getDouble("valor"),
                            rs.getString("dataDeLancamento"),
                            rs.getInt("qtdItens"),
                            rs.getBoolean("isLivro"),
                            rs.getInt("qtdPaginas")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livro;
    }

    public void update(Livro livro) {
        String query = "UPDATE Livros SET titulo = ?, criadoPor = ?, genero = ?, valor = ?, dataDeLancamento = ?, qtdItens = ?, qtdPaginas = ? WHERE ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getCriadoPor());
            ps.setString(3, livro.getGenero());
            ps.setDouble(4, livro.getValor());
            ps.setObject(5, livro.getDataDeLancamento());
            ps.setInt(6, livro.getQtdItens());
            ps.setInt(7, livro.getQtdPaginas());
            ps.setString(8, livro.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String ID) {
        String query = "DELETE FROM Livros WHERE ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}