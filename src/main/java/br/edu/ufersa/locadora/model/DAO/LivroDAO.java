package br.edu.ufersa.locadora.model.DAO;

import src.main.java.br.edu.ufersa.locadora.model.entities.Livro;

import java.util.List;
import java.util.ArrayList;

public class LivroDAO {
    public void create(Livro livro) {
        String query = "INSERT INTO Livros (titulo, criadoPor, genero, valor, dataDeLancamento, qtdItens, qtdPaginas) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getCriadoPor());
            ps.setString(3, livro.getGenero());
            ps.setDouble(4, livro.getValor());
            ps.setObject(5, livro.getDataDeLancamento());
            ps.setInt(6, livro.getQtdItens());
            ps.setInt(7, livro.getQtdPaginas());
            ps.executeUpdate();
        }
        catch (SQLException e) {e.printStackTrace();}
    }
    public List<Livro> read(String ID) {
        String query = "SELECT * FROM Livros WHERE ID = ?";
        List<Livro> livros = new ArrayList<>();
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                    Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("criadoPor"),
                        rs.getString("genero"),
                        rs.getDouble("valor"),
                        rs.getDate("dataDeLancamento"),
                        rs.getInt("qtdItens"),
                        rs.getInt("qtdPaginas")
                        );
                    livros.add(livro);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        return livros;
    }
    public Disco readByID(String ID) {
        String query = "SELECT * FROM Livros WHERE ID = ?";
        Livro livro = null;
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("criadoPor"),
                        rs.getString("genero"),
                        rs.getDouble("valor"),
                        rs.getDate("dataDeLancamento"),
                        rs.getInt("qtdItens"),
                        rs.getInt("qtdPaginas")
                        );
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        return livro;
    }
    public void update(Livro livro) {
        String query = "UPDATE Livros SET titulo = ?, criadoPor = ?, genero = ?, valor = ?, dataDeLancamento = ?, qtdItens = ?, qtdPaginas = ? WHERE ID = ?";
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
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
        }
        catch (SQLException e) {e.printStackTrace();}
        return livros;
    }
    public void delete(String ID) {
        String query = "DELETE FROM Livros WHERE ID = ?";
        
        try (Connection con = ConnectionFactory.getConnection(URL,USER,PASS); 
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, ID);
            ps.executeUpdate();
        }
        catch (SQLException e) {e.printStackTrace();}
    }
}
